package com.example.heartguardlayout

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.sin

class HeartbeatAnimation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.CYAN  // Set color of the heartbeat line
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val path: Path = Path()  // Path for the heartbeat line
    private var phase: Float = 0f  // Controls the phase of the animation (moving the wave)
    private var animator: ValueAnimator? = null
    private var animationDuration: Long = 2000  // Default duration for 60 BPM

    // Called when the View is being drawn
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        path.reset()  // Reset the path to start fresh each time

        val width = width.toFloat()
        val height = height.toFloat()

        path.moveTo(0f, height / 2)  // Start from the middle of the screen

        // Loop through all pixels in the width of the View and draw the heartbeat wave
        for (x in 0 until width.toInt()) {
            val t = (x + phase) / width * 4  // Create a time factor to control the heartbeat wave
            val y = height / 2 + calculateHeartbeat(t) * height / 3  // Calculate the Y position for the wave
            path.lineTo(x.toFloat(), y)
        }

        // Draw the heartbeat path on the canvas
        canvas.drawPath(path, paint)
    }

    // Function to calculate the heartbeat wave (sinusoidal wave with custom parameters)
    private fun calculateHeartbeat(t: Float): Float {
        val t2 = t % 1  // Keep the time factor in a 0-1 range to repeat the heartbeat cycle
        return when {
            t2 < 0.2 -> 1.5f * sin(t2 * Math.PI / 0.2f).toFloat()  // First rise
            t2 < 0.4 -> sin((t2 - 0.2f) * Math.PI / 0.1f).toFloat()  // The main beat
            t2 < 0.7 -> 0.3f * sin((t2 - 0.4f) * Math.PI / 0.3f).toFloat()  // Smaller oscillation
            else -> 0f  // Rest period
        }
    }

    // Start the animation that moves the wave
    fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, width.toFloat()).apply {
            duration = animationDuration
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                phase = animation.animatedValue as Float
                invalidate()  // Redraw the view on every update
            }
            start()
        }
    }

    // Update the animation speed based on BPM
    fun updateAnimationSpeed(bpm: Int) {
        animationDuration = (180000 / bpm).toLong() // Convert BPM to duration in ms
        startAnimation() // Restart animation with new duration
    }

    // Stop the animation when the view is detached from the window
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}
