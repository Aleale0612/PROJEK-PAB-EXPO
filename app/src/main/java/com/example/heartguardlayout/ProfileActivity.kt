package com.example.heartguardlayout

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

class ProfileActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    private lateinit var tvUsername: TextView
    private lateinit var tvBirthdate: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvAge: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mAuth = FirebaseAuth.getInstance()

        // Inisialisasi TextView untuk menampilkan data
        tvUsername = findViewById(R.id.tvUsername)
        tvBirthdate = findViewById(R.id.tvBirthdate)
        tvPhone = findViewById(R.id.tvPhone)
        tvAge = findViewById(R.id.tvAge)

        // Ambil userId dari Firebase Auth
        val userId = mAuth.currentUser?.uid

        // Pastikan pengguna sudah login dan UID tersedia
        if (userId != null) {
            // Ambil data dari Firestore
            getUserDataFromFirestore(userId)
        } else {
            // Jika pengguna belum login
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            finish()  // Hentikan activity jika user belum login
        }
    }

    private fun getUserDataFromFirestore(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                try {
                    if (document.exists()) {
                        // Ambil data birthdate, phone, dan age dari Firestore
                        val username = document.getString("username")
                        val birthdate = document.getString("birthdate")
                        val phone = document.getString("phone")
                        val age = document.getString("age") // Tidak melakukan konversi ke Int, tetap String

                        // Tampilkan data di TextView dengan null safety
                        tvUsername.text = username ?: "Tidak ada data"
                        tvBirthdate.text = birthdate ?: "Tidak ada data"
                        tvPhone.text = phone ?: "Tidak ada data"
                        tvAge.text = age ?: "Tidak ada data"

                        // Jika ingin memverifikasi, tampilkan di Toast
                        Toast.makeText(this, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Jika data tidak ditemukan di Firestore
                        Toast.makeText(this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Menangani kesalahan umum dalam mengambil atau memproses data
                    Toast.makeText(this, "Terjadi kesalahan: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // Menangani kesalahan Firestore atau masalah jaringan
                if (e is FirebaseFirestoreException) {
                    Toast.makeText(this, "Kesalahan Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal mengambil data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
