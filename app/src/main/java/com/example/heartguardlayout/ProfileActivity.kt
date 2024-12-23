package com.example.heartguardlayout

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

class ProfileActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    private lateinit var editTextUsername: EditText
    private lateinit var editTextBirthdate: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextAge: EditText
    private lateinit var logoutButton: Button
    private lateinit var buttonSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mAuth = FirebaseAuth.getInstance()

        // Inisialisasi elemen-elemen dari layout
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextBirthdate = findViewById(R.id.editTextBirthdate)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextAge = findViewById(R.id.editTextAge)
        logoutButton = findViewById(R.id.logoutButton)
        buttonSettings = findViewById(R.id.buttonSettings)

        // Ambil userId dari Firebase Auth
        val userId = mAuth.currentUser?.uid

        // Pastikan pengguna sudah login dan UID tersedia
        if (userId != null) {
            // Ambil data dari Firestore
            getUserDataFromFirestore(userId)
        } else {
            // Jika pengguna belum login
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            finish() // Hentikan activity jika user belum login
        }

        // Logout button listener
        logoutButton.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Settings button listener
        buttonSettings.setOnClickListener {
            Toast.makeText(this, "Settings clicked!", Toast.LENGTH_SHORT).show()
            // Tambahkan logika untuk membuka halaman pengaturan jika diperlukan
        }
    }

    private fun getUserDataFromFirestore(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                try {
                    if (document.exists()) {
                        // Ambil data dari Firestore
                        val username = document.getString("username")
                        val birthdate = document.getString("birthdate")
                        val phone = document.getString("phone")
                        val age = document.getString("age")

                        // Tampilkan data di EditText
                        editTextUsername.setText(username ?: "Tidak ada data")
                        editTextBirthdate.setText(birthdate ?: "Tidak ada data")
                        editTextPhone.setText(phone ?: "Tidak ada data")
                        editTextAge.setText(age ?: "Tidak ada data")

                        Toast.makeText(this, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Terjadi kesalahan: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                if (e is FirebaseFirestoreException) {
                    Toast.makeText(this, "Kesalahan Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal mengambil data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
