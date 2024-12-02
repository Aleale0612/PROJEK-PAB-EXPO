package com.example.heartguardlayout

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // Menambahkan listener untuk WindowInsets agar tampilan pas dengan layar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mendapatkan referensi ke komponen UI
        val ivProfilePicture = findViewById<ImageView>(R.id.ivProfilePicture)
        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val tvAge = findViewById<TextView>(R.id.tvAge)
        val tvMedicalHistory = findViewById<TextView>(R.id.tvMedicalHistory)

        // Contoh data profil
        tvUsername.text = "John Doe"
        tvAge.text = "Age: 29"
        tvMedicalHistory.text = "Medical History: Asthma, Allergies"

        // Jika menggunakan gambar profil, Anda bisa mengatur gambar di sini
        ivProfilePicture.setImageResource(R.drawable.profile_picture)  // Gantilah dengan gambar profil yang sesuai
    }
}
