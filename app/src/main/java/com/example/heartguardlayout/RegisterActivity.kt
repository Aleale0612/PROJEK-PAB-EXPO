package com.example.heartguardlayout

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Pindah ke halaman Register
        btnRegister.setOnClickListener {
            // Simulasi registrasi selesai
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

            // Pindah ke halaman Login setelah registrasi berhasil
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Menutup RegisterActivity setelah berpindah ke LoginActivity
            finish()
        }
    }
}
