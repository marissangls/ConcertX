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
import android.util.Patterns

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = DatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val password = etPassword.text.toString()

            if (username.isEmpty()) {
                Toast.makeText(this, "Username must be filled", Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty()) {
                Toast.makeText(this, "Email must be filled", Toast.LENGTH_SHORT).show()
            } else if (phone.isEmpty()) {
                Toast.makeText(this, "Phone number must be filled", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Password must be filled", Toast.LENGTH_SHORT).show()
            } else if (db.isUsernameExists(username)) {
                Toast.makeText(this, "Username already taken, try another username!", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email format is invalid", Toast.LENGTH_SHORT).show()
            } else if (!phone.all { it.isDigit() } || phone.length < 10 || phone.length > 13) {
                Toast.makeText(this, "Phone number must be 10-13 digits", Toast.LENGTH_SHORT).show()
            } else if (password.length < 5) {
                Toast.makeText(this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show()
            } else {
                val registered = db.insertUser(username, email, phone, password)

                if(registered) {
                    Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val tvLogin = findViewById<TextView>(R.id.tvLogin)
        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}