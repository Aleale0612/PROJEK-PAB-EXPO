package com.example.heartguardlayout

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        // Inisialisasi EditText dan Button
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val usernameEditText = findViewById<EditText>(R.id.etUsername)
        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val passEditText = findViewById<EditText>(R.id.etPassword)
        val birthDateEditText = findViewById<EditText>(R.id.etBirthDate) // EditText untuk tanggal lahir
        val phoneEditText = findViewById<EditText>(R.id.etPhoneNumber)
        val ageEditText = findViewById<EditText>(R.id.etAge)
        val medicalHistoryEditText = findViewById<EditText>(R.id.etMedicalHistory)

        // Listener untuk membuka DatePickerDialog
        birthDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Membuka DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)

                    birthDateEditText.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Handle register button
        btnRegister.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val birthdate = birthDateEditText.text.toString().trim()
            val age = ageEditText.text.toString().trim()
            val medicalHistory = medicalHistoryEditText.text.toString().trim().ifEmpty { "-" } // Default value if empty

            // Validasi input
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || birthdate.isEmpty()) {
                Toast.makeText(this, "Username, Email, Password, Phone number, dan Birthdate tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi format email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Registrasi akun dengan Firebase
            authRegis(username, email, password, phone, birthdate, age, medicalHistory)
        }
    }

    private fun authRegis(username: String, email: String, password: String, phone: String, birthdate: String, age: String, medicalHistory: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    user?.let {
                        val userId = it.uid

                        // Menyimpan data pengguna ke Firestore
                        saveUserDataToFirestore(userId, username, birthdate, phone, age, medicalHistory)

                        // Tampilkan pesan sukses
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                        // Pindah ke LoginActivity
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Menampilkan pesan kesalahan jika registrasi gagal
                    Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserDataToFirestore(userId: String, username: String, birthdate: String, phone: String, age: String, medicalHistory: String) {
        val user = hashMapOf(
            "username" to username,
            "birthdate" to birthdate,
            "phone" to phone,
            "age" to age,  // Age tetap disimpan sebagai String
            "medical_history" to medicalHistory // Menyimpan medical history ke Firestore
        )

        // Menyimpan data pengguna di Firestore dalam koleksi "users"
        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
