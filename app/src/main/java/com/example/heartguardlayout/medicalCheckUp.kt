package com.example.heartguardlayout

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.util.*

class medicalCheckUp : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var editTextDate: EditText
    lateinit var status: Spinner
    lateinit var submitButton: Button

    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()  // Firestore instance

    private var selectedImagePath: String? = null // Menyimpan path gambar yang dipilih

    // Register for image selection result (Activity Result API)
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Menampilkan gambar ke ImageView
            imageView.setImageURI(it)
            // Simpan gambar ke local storage
            selectedImagePath = saveImageToLocalStorage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_check_up)

        editTextDate = findViewById(R.id.editTextDate)
        imageView = findViewById(R.id.imageView)
        status = findViewById(R.id.spinner)
        submitButton = findViewById(R.id.button)

        mAuth = FirebaseAuth.getInstance()

        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        setupSpinner()

        imageView.setOnClickListener {
            getContent.launch("image/*")
        }

        submitButton.setOnClickListener {
            submitForm()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val dateString = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editTextDate.setText(dateString)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.status_array, // Referensi array di strings.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            status.adapter = adapter
        }
    }

    private fun saveImageToLocalStorage(imageUri: Uri): String? {
        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val file = File(getExternalFilesDir(null), "medical_image_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            return file.absolutePath // Mengembalikan path file yang disimpan
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    private fun saveToFirestore(
        userId: String,
        tanggalPeriksa: String,
        lokasiPeriksa: String,
        tensi: String,
        nadi: String,
        suhu: String,
        beratBadan: String,
        tinggiBadan: String,
        statusSelected: String,
        obat: String,
        diagnosis: String,
        localImagePath: String? = null // Menambahkan path lokal gambar jika ada
    ) {
        val medicalRecord = hashMapOf(
            "tanggal_periksa" to tanggalPeriksa,
            "lokasi_periksa" to lokasiPeriksa,
            "tensi" to tensi,
            "nadi" to nadi,
            "suhu" to suhu,
            "berat_badan" to beratBadan,
            "tinggi_badan" to tinggiBadan,
            "status" to statusSelected,
            "obat_obatan" to obat,
            "diagnosis" to diagnosis
        )

        // Jika ada gambar, tambahkan path lokal ke data
        localImagePath?.let {
            medicalRecord["image_path"] = it
        }

        db.collection("users").document(userId)
            .collection("medical_records")
            .add(medicalRecord)
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
    }

    private fun submitForm() {
        val userId = mAuth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "Anda belum login!", Toast.LENGTH_SHORT).show()
            return
        }

        val tanggalPeriksa = editTextDate.text.toString()
        val lokasiPeriksa = findViewById<EditText>(R.id.editTextText5).text.toString()
        val tensi = findViewById<EditText>(R.id.editTextTensi).text.toString()
        val nadi = findViewById<EditText>(R.id.editTextNadi).text.toString()
        val suhu = findViewById<EditText>(R.id.editTextSuhu).text.toString()
        val beratBadan = findViewById<EditText>(R.id.editTextBeratBadan).text.toString()
        val tinggiBadan = findViewById<EditText>(R.id.editTextTinggiBadan).text.toString()
        val statusSelected = status.selectedItem.toString()
        val obat = findViewById<EditText>(R.id.editTextText6).text.toString()
        val diagnosis = findViewById<EditText>(R.id.editTextText2).text.toString()

        if (tanggalPeriksa.isEmpty() || lokasiPeriksa.isEmpty() || tensi.isEmpty() || diagnosis.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom yang wajib!", Toast.LENGTH_SHORT).show()
            return
        }

        // Simpan data ke Firestore
        saveToFirestore(
            userId,
            tanggalPeriksa,
            lokasiPeriksa,
            tensi,
            nadi,
            suhu,
            beratBadan,
            tinggiBadan,
            statusSelected,
            obat,
            diagnosis,
            selectedImagePath // Menambahkan path gambar ke Firestore
        )
    }
}


