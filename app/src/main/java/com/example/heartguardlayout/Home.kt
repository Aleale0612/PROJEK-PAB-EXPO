package com.example.heartguardlayout

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Home : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inisialisasi tombol dari XML
        val profileButton = findViewById<FloatingActionButton>(R.id.profile)
        val tambahDataMCUButton = findViewById<FloatingActionButton>(R.id.TambahdataMCU)
        val bmiCalButton = findViewById<ImageButton>(R.id.bmiCal)
        val heartRateButton = findViewById<ImageButton>(R.id.heartRate)
        val medicalRecordButton = findViewById<ImageButton>(R.id.medicalRecord)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // Inisialisasi TextView untuk menampilkan username
        val helloTextView = findViewById<TextView>(R.id.namaUser)

        mAuth = FirebaseAuth.getInstance()

        // Mendapatkan UID dari pengguna yang sedang login
        val userId = mAuth.currentUser?.uid

        if (userId != null) {
            // Ambil data pengguna dari Firestore
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val username = document?.getString("username") ?: "Guest"
                    helloTextView.text = username
                }
                .addOnFailureListener {
                    helloTextView.text = "Error loading user"
                }
        } else {
            helloTextView.text = "No user logged in"
        }

        // Fungsi klik untuk tombol "Profile"
        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Fungsi klik untuk tombol "Tambah Data MCU"
        tambahDataMCUButton.setOnClickListener {
            startActivity(Intent(this, medicalCheckUp::class.java))
        }

        // Fungsi klik untuk tombol "BMI Calculator"
        bmiCalButton.setOnClickListener {
            startActivity(Intent(this, BMIActivity::class.java))
        }

        // Fungsi klik untuk tombol "Heart Rate"
        heartRateButton.setOnClickListener {
            startActivity(Intent(this, HeartbeatGraphView::class.java))
        }

        // Fungsi klik untuk tombol "Medical Record"
        medicalRecordButton.setOnClickListener {
            startActivity(Intent(this, detailOfMCU::class.java))
        }

        // Fungsi klik untuk tombol "Logout"
        logoutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
