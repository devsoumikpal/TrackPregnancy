package com.example.callstatemonitor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.PROCESS_OUTGOING_CALLS
    )

    private var isCallActive = false
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var customPhoneStateListener: CustomPhoneStateListener

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkPermissions()) {
            startCallTracking()
        } else {
            ActivityCompat.requestPermissions(this, permissions, 101)
        }
    }

    // State colors
    private val stateColors = mapOf(
        "RINGING" to Color.RED,
        "CONNECTED" to Color.GREEN,
        "WAITING" to Color.YELLOW,
        "IDLE" to Color.WHITE
    )

    private fun checkPermissions(): Boolean {
        permissions.forEach {
            Log.d("PermissionStatus", "$it: ${ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED}")
        }
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun startCallTracking() {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        customPhoneStateListener = CustomPhoneStateListener(this)

        // Use reflection to check for LISTEN_CALL_WAITING
        telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    //fun outgoing call initiation
    fun trackOutgoingCall(){
        customPhoneStateListener.trackOutgoingCall()
    }

    fun startSpeechRecognition() {
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) // Enable partial results
        }

        // Define the RecognitionListener
        val listener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d("SpeechRecognition", "Ready for speech")
            }

            override fun onBeginningOfSpeech() {
                Log.d("SpeechRecognition", "Beginning of speech")
            }

            override fun onRmsChanged(rmsdB: Float) {
                Log.d("SpeechRecognition", "RMS changed: $rmsdB")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Handle raw audio buffer if needed
            }

            override fun onEndOfSpeech() {
                Log.d("SpeechRecognition", "End of speech")
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                    else -> "Unknown error"
                }
                Log.e("SpeechRecognition", "Error: $errorMessage")
                showAlert("ERROR", errorMessage)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.forEach { text ->
                    Log.d("SpeechRecognition", "Final Result: $text")
                    if (text.contains("busy", ignoreCase = true)) {
                        showAlert("ANNOUNCEMENT", "The person is busy")
                    }
                }

                // Restart speech recognition if the call is still active
                if (isCallActive) {
                    startSpeechRecognition()
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.forEach { text ->
                    Log.d("SpeechRecognition", "Partial Result: $text")
                    if (text.contains("busy", ignoreCase = true)) {
                        showAlert("ANNOUNCEMENT", "The person is busy")
                    }
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Reserved for future use
            }
        }

        // Set the listener for the SpeechRecognizer
        speechRecognizer.setRecognitionListener(listener)

        // Start listening
        speechRecognizer.startListening(speechIntent)
    }

    fun showAlert(state: String, message: String) {
        runOnUiThread {
            // Update status text
            findViewById<TextView>(R.id.tvStatus).apply {
                text = message
                setTextColor(stateColors[state] ?: Color.WHITE)
            }

            // Update icon
            val iconRes = when (state) {
                "RINGING" -> R.drawable.call_ringing
                "CONNECTED" -> R.drawable.call_connected
                else -> R.drawable.baseline_call_24
            }
            findViewById<ImageView>(R.id.ivStatusIcon).setImageResource(iconRes)

            // Add to history
            findViewById<TextView>(R.id.tvHistory).apply {
                append("${SimpleDateFormat("HH:mm:ss").format(Date())} - $message\n")
            }
        }
    }

    fun resetStatus() {
        runOnUiThread {
            // Reset status text
            findViewById<TextView>(R.id.tvStatus).apply {
                text = "Ready"
                setTextColor(Color.WHITE)
            }

            // Reset icon
            findViewById<ImageView>(R.id.ivStatusIcon).setImageResource(R.drawable.baseline_call_24)

            // Clear history
            findViewById<TextView>(R.id.tvHistory).text = ""
        }
    }

    fun startVibration(state: String) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (!vibrator.hasVibrator()) return

        when (state) {
            "RINGING" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(500, 1000), 0))
                } else {
                    vibrator.vibrate(longArrayOf(500, 1000), 0)
                }
            }
            "WAITING" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(1500)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            startCallTracking()
        }
    }
}