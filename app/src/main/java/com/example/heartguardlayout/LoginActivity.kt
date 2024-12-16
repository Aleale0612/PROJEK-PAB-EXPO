package com.example.heartguardlayout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validasi input email dan password
            if (email.isEmpty()) {
                etEmail.error = "Email tidak boleh kosong"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Password tidak boleh kosong"
                return@setOnClickListener
            }

            // Login menggunakan Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Ambil userId dari Firebase Auth
                        val userId = mAuth.currentUser?.uid

                        if (userId != null) {
                            // Ambil data dari Firestore
                            getUserDataFromFirestore(userId)
                        }

                        // Pindah ke halaman Home setelah login berhasil
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        tvRegister.setOnClickListener {
            // Arahkan pengguna ke halaman register
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun getUserDataFromFirestore(userId: String) {
        // Mengambil data dari Firestore berdasarkan userId
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val birthdate = document.getString("birthdate")
                    val phone = document.getString("phone")
                    val age = document.getLong("age")

                    // Menyimpan data ke SharedPreferences atau menggunakan data ini di aplikasi
                    val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("birthdate", birthdate)
                    editor.putString("phone", phone)
                    editor.putInt("age", age?.toInt() ?: -1)
                    editor.apply()

                    // Menampilkan data di log untuk debugging
                    Toast.makeText(this, "Data fetched successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // Menangani kesalahan jika gagal mengambil data
                Toast.makeText(this, "Error getting data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
