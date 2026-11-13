package com.example.concertx.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.concertx.R
import com.example.concertx.data.local.DatabaseHelper
import android.widget.EditText
import android.widget.Button
import android.widget.Toast

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

import com.example.concertx.data.remote.SmsSender

class LoginActivity : AppCompatActivity() {
    private var pendingUsername = ""
    private val smsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {granted ->
        if(granted){
            proceedToOtp(pendingUsername)
        }else {
            Toast.makeText(this, "SMS Permission is Required!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun proceedToOtp(username: String){
        val db = DatabaseHelper(this)
        // ambil nomor telepon utk bisa kirim sms
        val phone = db.getPhoneByUsername(username)

        if(phone == null){
            Toast.makeText(this, "Phone Number NOt Found", Toast.LENGTH_SHORT).show()
            return
        }

        val otp = (100000..999999).random().toString()

        val sent = SmsSender.sendOtp(this, phone, otp)
        if(!sent){
            Toast.makeText(this, "Failed to send OTP", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra("otp", otp)
        intent.putExtra("sentAt", System.currentTimeMillis())
        intent.putExtra("username", username)
        startActivity(intent)

        Toast.makeText(this, "test dlu ini otpnye: $otp", Toast.LENGTH_LONG).show()
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = DatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()

            if (username.isEmpty()) {
                Toast.makeText(this, "Username must be filled", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Password must be filled", Toast.LENGTH_SHORT).show()
            } else if (!db.isUsernameExists(username)) {
                Toast.makeText(this, "Username is not registered", Toast.LENGTH_SHORT).show()
            } else if (!db.checkLogin(username, password)) {
                Toast.makeText(this, "Password is incorrect", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()
//
//                val otp = (100000..999999).random().toString()
//                val intent = Intent(this, OtpActivity::class.java)
//                intent.putExtra("otp", otp)
//                intent.putExtra("sentAt", System.currentTimeMillis())
//                startActivity(intent)
//
//                Toast.makeText(this, "test dlu ini otpnye: $otp", Toast.LENGTH_LONG).show()
//                finish()

                pendingUsername = username
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    proceedToOtp(username)
                }else{
                    smsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                }
            }
        }

        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}