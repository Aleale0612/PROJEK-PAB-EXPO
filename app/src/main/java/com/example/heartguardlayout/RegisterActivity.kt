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
        val usernameEditText = findViewById<EditText>(R.id.etUsername) // EditText untuk username
        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val passEditText = findViewById<EditText>(R.id.etPassword)
        val birthDateEditText = findViewById<EditText>(R.id.etBirthDate)
        val phoneEditText = findViewById<EditText>(R.id.etPhoneNumber)

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

        btnRegister.setOnClickListener {
            val username = usernameEditText.text.toString().trim() // Ambil username
            val email = emailEditText.text.toString().trim()
            val password = passEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val birthdate = birthDateEditText.text.toString().trim()

            // Validasi input
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || birthdate.isEmpty()) {
                Toast.makeText(this, "Username, Email, Password, Phone number, dan Birthdate tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Registrasi akun dengan Firebase
            authRegis(username, email, password, phone, birthdate)
        }
    }

    private fun authRegis(username: String, email: String, password: String, phone: String, birthdate: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    user?.let {
                        val userId = it.uid

                        // Menghitung usia berdasarkan birthdate
                        val age = calculateAge(birthdate)

                        // Menyimpan data pengguna ke Firestore
                        saveUserDataToFirestore(userId, username, birthdate, phone, age)

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

    private fun saveUserDataToFirestore(userId: String, username: String, birthdate: String, phone: String, age: Int) {
        val user = hashMapOf(
            "username" to username,   // Simpan username ke Firestore
            "birthdate" to birthdate, // birthdate dalam bentuk string
            "phone" to phone,
            "age" to age
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

    // Fungsi untuk menghitung usia berdasarkan birthdate
    private fun calculateAge(birthdate: String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val birthDate = dateFormat.parse(birthdate)
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time

        val birthCalendar = Calendar.getInstance()
        birthCalendar.time = birthDate

        var age = currentDate.year - birthCalendar.time.year
        if (currentDate.month < birthCalendar.time.month || (currentDate.month == birthCalendar.time.month && currentDate.date < birthCalendar.time.date)) {
            age--
        }
        return age
    }
}
