package com.example.heartguardlayout

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.database.*

class HeartbeatGraphView : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var bpmTextView: TextView
    private lateinit var bpmIndicator: CircularProgressIndicator
    private var bpmAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heartbeat_graph_view)

        bpmTextView = findViewById(R.id.bpmTextView)
        bpmIndicator = findViewById(R.id.bpmIndicator)

        // Inisialisasi Firebase dengan URL Realtime Database yang benar
        val databaseUrl = "https://dbheartguard-default-rtdb.asia-southeast1.firebasedatabase.app" // URL yang benar
        database = FirebaseDatabase.getInstance(databaseUrl).reference

        // Tambahkan animasi detak pada CircularProgressIndicator
        startHeartbeatAnimation()

        // Membaca data dari Firebase Realtime Database
        database.child("bpm").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bpm = snapshot.getValue(Int::class.java)
                bpm?.let {
                    updateBPM(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                bpmTextView.text = "Error: ${error.message}"
            }
        })
    }

    private fun updateBPM(newBPM: Int) {
        bpmAnimator?.cancel()
        bpmAnimator = ValueAnimator.ofInt(bpmIndicator.progress, newBPM).apply {
            duration = 1500 // Tambah durasi animasi jadi 1,5 detik
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                val animatedBPM = animation.animatedValue as Int
                bpmIndicator.progress = animatedBPM
                bpmTextView.text = "$animatedBPM BPM"
                bpmTextView.setTextColor(Color.rgb(255, 69, 58)) // Ubah warna teks
            }
            start()
        }

    }

    private fun startHeartbeatAnimation() {
        val scaleUpX = ObjectAnimator.ofFloat(bpmIndicator, "scaleX", 1f, 1.1f)
        val scaleUpY = ObjectAnimator.ofFloat(bpmIndicator, "scaleY", 1f, 1.1f)

        scaleUpX.duration = 500
        scaleUpY.duration = 500

        scaleUpX.repeatCount = ValueAnimator.INFINITE
        scaleUpY.repeatCount = ValueAnimator.INFINITE

        scaleUpX.repeatMode = ValueAnimator.REVERSE
        scaleUpY.repeatMode = ValueAnimator.REVERSE

        scaleUpX.start()
        scaleUpY.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        bpmAnimator?.cancel()
    }
}
