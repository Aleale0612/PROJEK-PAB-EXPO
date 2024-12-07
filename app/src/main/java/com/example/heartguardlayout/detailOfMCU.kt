package com.example.heartguardlayout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class detailOfMCU : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_of_mcu)

        // FloatingActionButton
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            // Intent untuk beralih ke halaman MedicalCheckUpActivity
            val intent = Intent(this, medicalCheckUp::class.java)
            startActivity(intent)
        }
    }
}
