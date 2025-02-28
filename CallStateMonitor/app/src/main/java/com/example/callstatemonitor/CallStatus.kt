package com.example.callstatemonitor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CallStatus : AppCompatActivity() {
    private lateinit var callStatusTracker: CallStatusTracker
    private lateinit var statusIcon: ImageView
    private lateinit var statusText: TextView
    private lateinit var alertEffect: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_status)

        statusIcon = findViewById(R.id.statusIcon)
        statusText = findViewById(R.id.statusText)
        alertEffect = findViewById(R.id.alertEffect) // Initialize alert effect view

        checkPermission()
    }

    private fun checkPermission() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG
        )
        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missingPermissions.toTypedArray(), 1)
        } else {
            startTracking()
        }
    }

    // Add proper permission handling
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            startTracking()
        } else {
            finish() // Close app if permissions denied
        }
    }

    private fun startTracking() {
        callStatusTracker = CallStatusTracker(this) { status ->
            runOnUiThread {
                when (status) {
                    "Announcement" -> {
                        statusText.text = "🔊 Announcement Playing..."
                        statusIcon.setImageResource(R.drawable.announcement)
                        alertEffect.visibility = View.VISIBLE
                        alertEffect.animate().alpha(0.5f).setDuration(500).start()
                    }
                    "Ringing" -> {
                        statusText.text = "📞 Ringing..."
                        statusIcon.setImageResource(R.drawable.call_ringing)
                        alertEffect.visibility = View.VISIBLE
                        startPulseAnimation()
                    }
                    "Connected" -> {
                        statusText.text = "✅ Connected"
                        statusIcon.setImageResource(R.drawable.call_connected)
                        alertEffect.visibility = View.GONE
                    }
                    "Disconnected" -> {
                        statusText.text = "❌ Disconnected"
                        statusIcon.setImageResource(R.drawable.call_disconnected)
                        alertEffect.visibility = View.GONE
                    }
                }
            }
        }
        callStatusTracker.startListening()
    }

    private fun startPulseAnimation() {
        alertEffect.animate()
            .scaleX(1.5f)
            .scaleY(1.5f)
            .alpha(0.7f)
            .setDuration(500)
            .withEndAction {
                alertEffect.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(0.2f)
                    .setDuration(500)
                    .start()
            }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        callStatusTracker.stopListening()
    }
}