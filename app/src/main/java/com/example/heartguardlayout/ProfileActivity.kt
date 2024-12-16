package com.example.heartguardlayout

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    private lateinit var tvBirthdate: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvAge: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mAuth = FirebaseAuth.getInstance()

        // Inisialisasi TextView untuk menampilkan data
        tvBirthdate = findViewById(R.id.tvBirthdate)
        tvPhone = findViewById(R.id.tvPhone)
        tvAge = findViewById(R.id.tvAge)

        // Ambil userId dari Firebase Auth
        val userId = mAuth.currentUser?.uid

        if (userId != null) {
            // Ambil data dari Firestore
            getUserDataFromFirestore(userId)
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserDataFromFirestore(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Ambil data birthdate, phone, dan age dari Firestore
                    val birthdate = document.getString("birthdate")
                    val phone = document.getString("phone")
                    val age = document.getLong("age")?.toInt()

                    // Tampilkan data di TextView
                    tvBirthdate.text = birthdate ?: "Tidak ada data"
                    tvPhone.text = phone ?: "Tidak ada data"
                    tvAge.text = age?.toString() ?: "Tidak ada data"

                    // Jika ingin memverifikasi, tampilkan di Toast
                    Toast.makeText(this, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal mengambil data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
