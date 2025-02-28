package com.example.callstatemonitor

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi

class CallStatusTracker(private val context: Context, private val onStatusChange: (String) -> Unit) {

    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private var isRingingDetected = false
    private val handler = Handler(Looper.getMainLooper())

    private val callListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            when (state) {
                TelephonyManager.CALL_STATE_IDLE -> {
                    Log.d("CallStatus", "❌ Call Disconnected")
                    handler.post { onStatusChange("Disconnected") }
                    handler.post { onStatusChange("Disconnected") }
                    handler.postDelayed(:: detectAnnouncement, 1000)
                }
                TelephonyManager.CALL_STATE_RINGING -> {
                    Log.d("CallStatus", "📞 Ringing...")
                    isRingingDetected = true
                    handler.post { onStatusChange("Ringing") }
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    Log.d("CallStatus", "✅ Call Connected")
                    handler.post { onStatusChange("Connected") }
                }
            }
        }
    }

    fun startListening() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Use executor for newer Android versions
            startListeningForAndroid12Plus()
        } else {
            startListeningLegacy()
        }
    }

    private fun startListeningLegacy() {
        telephonyManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startListeningForAndroid12Plus() {
        telephonyManager.listen(
            callListener, PhoneStateListener.LISTEN_CALL_STATE
        )
    }

    fun stopListening() {
        telephonyManager.listen(callListener, PhoneStateListener.LISTEN_NONE)
    }

    private fun detectAnnouncement() {
        if (!isRingingDetected) {
            Log.d("CallStatus", "🔊 Announcement Detected")
            handler.post { onStatusChange("Announcement") }
        }
        isRingingDetected = false
    }
}

