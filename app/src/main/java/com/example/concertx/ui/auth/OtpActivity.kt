package com.example.concertx.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.example.concertx.R
import com.example.concertx.ui.home.HomeActivity
import com.example.concertx.data.local.SessionManager

class OtpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_otp)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var otpCode = intent.getStringExtra("otp") ?: ""
        var sentAt = intent.getLongExtra("sentAt", 0L)
        val username = intent.getStringExtra("username") ?: ""

        val etOtp1 = findViewById<EditText>(R.id.etOtp1)
        val etOtp2 = findViewById<EditText>(R.id.etOtp2)
        val etOtp3 = findViewById<EditText>(R.id.etOtp3)
        val etOtp4 = findViewById<EditText>(R.id.etOtp4)
        val etOtp5 = findViewById<EditText>(R.id.etOtp5)
        val etOtp6 = findViewById<EditText>(R.id.etOtp6)
        val btnVerify = findViewById<Button>(R.id.btnVerify)

        etOtp1.doAfterTextChanged {
            if (it?.length == 1) {
                etOtp2.requestFocus()
            }
        }
        etOtp2.doAfterTextChanged {
            if (it?.length == 1) {
                etOtp3.requestFocus()
            }
        }
        etOtp3.doAfterTextChanged {
            if (it?.length == 1) {
                etOtp4.requestFocus()
            }
        }
        etOtp4.doAfterTextChanged {
            if (it?.length == 1) {
                etOtp5.requestFocus()
            }
        }
        etOtp5.doAfterTextChanged {
            if (it?.length == 1) {
                etOtp6.requestFocus()
            }
        }

        btnVerify.setOnClickListener {
            val otp = etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString() + etOtp5.text.toString() + etOtp6.text.toString()

            if (otp.length != 6) {
                Toast.makeText(this, "OTP must be filled", Toast.LENGTH_SHORT).show()
            } else if (otp != otpCode) {
                Toast.makeText(this, "OTP is incorrect", Toast.LENGTH_SHORT).show()
            } else if (System.currentTimeMillis() > sentAt + (3 * 60 * 1000)) {
                Toast.makeText(this, "OTP has expired", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "OTP verified", Toast.LENGTH_SHORT).show()

                SessionManager(this).saveSession(username)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        val tvResend = findViewById<TextView>(R.id.tvResend)
        tvResend.setOnClickListener {
            otpCode = (100000..999999).random().toString()
            sentAt = System.currentTimeMillis()
            Toast.makeText(this, "ni otp yg neww new $otpCode", Toast.LENGTH_SHORT).show()
        }
    }
}