package com.example.heartguardlayout

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.database.*

class HeartbeatGraphView : AppCompatActivity() {

    private lateinit var bpmTextView: TextView
    private lateinit var bpmIndicator: CircularProgressIndicator
    private lateinit var database: DatabaseReference
    private var bpmAnimator: ValueAnimator? = null

    private lateinit var heartbeatAnimation: HeartbeatAnimation // New heartbeat graph view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heartbeat_graph_view)

        // Menyambungkan view
        bpmTextView = findViewById(R.id.bpmTextView)
        bpmIndicator = findViewById(R.id.animasi)

        // Inisialisasi Firebase
        val databaseUrl = "https://dbheartguard-default-rtdb.asia-southeast1.firebasedatabase.app"
        database = FirebaseDatabase.getInstance(databaseUrl).reference

        // Menambahkan animasi detak jantung pada CircularProgressIndicator
        startHeartbeatAnimation()

        // Menyambungkan HeartbeatAnimation untuk grafik bawah
        heartbeatAnimation = findViewById(R.id.heartbeatGraphView)

        // Membaca data BPM dari Firebase
        database.child("bpm").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bpm = snapshot.getValue(Int::class.java)
                bpm?.let {
                    updateBPM(it)
                    heartbeatAnimation.updateAnimationSpeed(it)  // Update the heartbeat graph animation
                }
            }

            override fun onCancelled(error: DatabaseError) {
                bpmTextView.text = "Error: ${error.message}"
            }
        })
    }

    // Fungsi untuk memperbarui nilai BPM di UI
    private fun updateBPM(newBPM: Int) {
        bpmAnimator?.cancel()
        bpmAnimator = ValueAnimator.ofInt(bpmIndicator.progress, newBPM).apply {
            duration = 1500 // Durasi animasi perubahan BPM
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                val animatedBPM = animation.animatedValue as Int
                bpmIndicator.progress = animatedBPM
                bpmTextView.text = "$animatedBPM BPM"
                bpmTextView.setTextColor(Color.rgb(255, 69, 58)) // Ubah warna teks ke merah
            }
            start()
        }
    }

    // Memulai animasi detak jantung pada CircularProgressIndicator
    private fun startHeartbeatAnimation() {
        val scaleUpX = ObjectAnimator.ofFloat(bpmIndicator, "scaleX", 1f, 1.1f)
        val scaleUpY = ObjectAnimator.ofFloat(bpmIndicator, "scaleY", 1f, 1.1f)

        scaleUpX.duration = 500
        scaleUpY.duration = 500

        scaleUpX.repeatCount = ValueAnimator.INFINITE
        scaleUpY.repeatCount = ValueAnimator.INFINITE

        scaleUpX.repeatMode = ValueAnimator.REVERSE
        scaleUpY.repeatMode = ValueAnimator.REVERSE

        // Menambahkan interpolator agar animasi berulang terus
        scaleUpX.interpolator = LinearInterpolator()
        scaleUpY.interpolator = LinearInterpolator()

        // Mulai animasi
        scaleUpX.start()
        scaleUpY.start()
    }

    override fun onResume() {
        super.onResume()
        // Restart the heartbeat animation when the activity is resumed
        bpmAnimator?.start()
        heartbeatAnimation.startAnimation() // Restart the graph animation if it's paused
    }
}
