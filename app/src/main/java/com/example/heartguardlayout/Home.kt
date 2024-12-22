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
        setContentView(R.layout.activity_home) // Menggunakan layout activity_home.xml

        // Inisialisasi tombol dari XML
        val profileButton = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.profile)
        val tambahDataMCUButton = findViewById<FloatingActionButton>(R.id.TambahdataMCU)
        val bmiCalButton = findViewById<ImageButton>(R.id.bmiCal)
        val heartRateButton = findViewById<ImageButton>(R.id.heartRate)
        val medicalRecordButton = findViewById<ImageButton>(R.id.medicalRecord)
        val logoutButton = findViewById<Button>(R.id.logoutButton) // Tombol logout

        // Inisialisasi TextView untuk menampilkan username
        val helloTextView = findViewById<TextView>(R.id.namaUser)

        mAuth = FirebaseAuth.getInstance()

        // Mendapatkan UID dari pengguna yang sedang login
        val userId = mAuth.currentUser?.uid

        // Pastikan pengguna sudah login dan UID tersedia
        if (userId != null) {
            // Ambil data pengguna dari Firestore
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Mengambil username dari Firestore
                        val username = document.getString("username")
                        // Menampilkan username pada TextView
                        helloTextView.text = "$username"
                    }
                }
                .addOnFailureListener { exception ->
                    helloTextView.text = "Null"
                }
        } else {
            helloTextView.text = "Null"
        }

        // Fungsi klik untuk tombol "Profile"
        profileButton.setOnClickListener {
            // Pindah ke halaman ProfileActivity
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        tambahDataMCUButton.setOnClickListener {
            // Intent untuk pindah ke halaman medicalCheckUp
            val intent = Intent(this, medicalCheckUp::class.java)
            startActivity(intent)
        }

        // Fungsi klik untuk tombol "BMI" (Jika diperlukan, Anda bisa membuka halaman BMI)
        bmiCalButton.setOnClickListener {
            // Contoh: pindah ke halaman BMIActivity (buat kelas BMIActivity jika diperlukan)
            // val intent = Intent(this, BMIActivity::class.java)
            // startActivity(intent)
        }

        // Fungsi klik untuk tombol "Heart Rate" (Jika diperlukan, Anda bisa membuka halaman HeartRate)
        heartRateButton.setOnClickListener {
            // Contoh: pindah ke halaman HeartRateActivity (buat kelas HeartRateActivity jika diperlukan)
            val intent = Intent(this, HeartbeatGraphView::class.java)
            startActivity(intent)
        }

        // Fungsi klik untuk tombol "Medical Record"
        medicalRecordButton.setOnClickListener {
            // Intent untuk pindah ke halaman Catatan Medis
            val intent = Intent(this, detailOfMCU::class.java)
            startActivity(intent)
        }
        // Fungsi klik untuk tombol "Logout"
        logoutButton.setOnClickListener {
            // Melakukan logout
            mAuth.signOut()
            // Mengalihkan pengguna ke halaman Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Menutup HomeActivity sehingga pengguna tidak bisa kembali ke halaman ini setelah logout
        }
    }
}
