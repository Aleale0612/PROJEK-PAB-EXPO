package com.example.heartguardlayout

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.util.*

class medicalCheckUp : AppCompatActivity() {

     val PICK_IMAGE_REQUEST = 1
     lateinit var imageView: ImageView
     lateinit var editTextDate: EditText
     lateinit var status: Spinner
     lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_check_up)

        // Inisialisasi komponen sesuai ID yang ada di XML
        editTextDate = findViewById(R.id.editTextDate) // EditText untuk tanggal
        imageView = findViewById(R.id.imageView) // ImageView untuk upload gambar
        status = findViewById(R.id.spinner) // Spinner untuk status
        submitButton = findViewById(R.id.button) // Button untuk submit

        // 1. Fungsi DatePicker untuk tanggal
        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        // 2. Setup Spinner (dropdown)
        setupSpinner()

        // 3. Fungsi Upload Gambar
        imageView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST)
        }

        // 4. Fungsi tombol submit
        submitButton.setOnClickListener {
            submitForm()
        }
    }

    // Fungsi DatePicker untuk input tanggal
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

    // Setup Spinner dengan pilihan status
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

    // Fungsi untuk upload gambar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri: Uri? = data.data
            imageView.setImageURI(uri)
        }
    }


    // Fungsi untuk submit form (bisa dikembangkan lebih lanjut)
    private fun submitForm() {
        val diagnosis = findViewById<EditText>(R.id.editTextText2).text.toString()
        val date = editTextDate.text.toString()
        val status = status .selectedItem.toString()
        val lokasiPeriksa = findViewById<EditText>(R.id.editTextText5).text.toString()
        val obat = findViewById<EditText>(R.id.editTextText6).text.toString()

        // Bisa kirim data ke server, atau log data yang diinputkan
        Toast.makeText(this, "Form submittedd!\nDiagnosis: $diagnosis\nTanggal: $date\nStatus: $status", Toast.LENGTH_LONG).show()
    }
}
