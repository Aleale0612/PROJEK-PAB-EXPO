package com.example.heartguardlayout

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BMIActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        // Referensi elemen UI
        val heightEditText = findViewById<EditText>(R.id.heightEditText)
        val weightEditText = findViewById<EditText>(R.id.weightEditText)
        val calcButton = findViewById<Button>(R.id.button)
        val clearButton = findViewById<Button>(R.id.clearButton)
        val bmiInfoTextView = findViewById<TextView>(R.id.infoBMITextView)

        bmiInfoTextView.visibility = View.GONE

        calcButton.setOnClickListener {
            val heightText = heightEditText.text.toString()
            val weightText = weightEditText.text.toString()

            if (heightText.isEmpty() || weightText.isEmpty()) {
                Toast.makeText(this, getString(R.string.input_error), Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val height = heightText.toDouble() / 100 // Convert to meters
                    val weight = weightText.toDouble()

                    if (height > 0 && weight > 0) {
                        val bmi = weight / (height * height)
                        val category = when {
                            bmi < 18.5 -> getString(R.string.underweight)
                            bmi < 24.9 -> getString(R.string.normal)
                            bmi < 29.9 -> getString(R.string.overweight)
                            else -> getString(R.string.obese)
                        }
                        val bmiMessage = getString(R.string.bmi_info, String.format("%.2f", bmi), category)
                        bmiInfoTextView.text = bmiMessage
                        bmiInfoTextView.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(this, getString(R.string.input_error), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, getString(R.string.input_error), Toast.LENGTH_SHORT).show()
                }
            }
        }

        clearButton.setOnClickListener {
            heightEditText.text.clear()
            weightEditText.text.clear()
            bmiInfoTextView.visibility = View.GONE
        }
    }
}
