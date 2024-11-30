package com.example.heartguardlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Menggunakan layout activity_main.xml

        // Inisialisasi tombol dari XML
        val bmiCalButton = findViewById<Button>(R.id.bmiCal)
        val heartRateButton = findViewById<Button>(R.id.heartRate)
        val medicalRecordButton = findViewById<Button>(R.id.medicalRecord)

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

        // Fungsi klik untuk tombol "Medical Record"
        medicalRecordButton.setOnClickListener {
            // Intent untuk pindah ke halaman Catatan Medis
            val intent = Intent(this, Catatan_Medis::class.java)
            startActivity(intent)
        }
    }
}
