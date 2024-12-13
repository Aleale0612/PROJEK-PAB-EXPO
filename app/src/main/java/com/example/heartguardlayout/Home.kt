package com.example.heartguardlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Menggunakan layout activity_main.xml

        // Inisialisasi tombol dari XML
        val profileButton = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.profile)
        val bmiCalButton = findViewById<ImageButton>(R.id.bmiCal)
        val heartRateButton = findViewById<ImageButton>(R.id.heartRate)
        val medicalRecordButton = findViewById<ImageButton>(R.id.medicalRecord)


        // Fungsi klik untuk tombol "Profile"
        profileButton.setOnClickListener {
            // Pindah ke halaman ProfileActivity
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }


        // Fungsi klik untuk tombol "News"
        bmiCalButton.setOnClickListener {
            // Contoh: pindah ke halaman News (buat kelas NewsActivity jika diperlukan)
            // val intent = Intent(this, NewsActivity::class.java)
            // startActivity(intent)
        }

        // Fungsi klik untuk tombol "Heart Rate"
        heartRateButton.setOnClickListener {
            // Contoh: pindah ke halaman HeartRate (buat kelas HeartRateActivity jika diperlukan)
            // val intent = Intent(this, HeartRateActivity::class.java)
            // startActivity(intent)
        }

        medicalRecordButton.setOnClickListener {
            // Intent untuk pindah ke halaman Catatan Medis
            val intent = Intent(this, detailOfMCU::class.java)
            startActivity(intent)
        }

    }
}
