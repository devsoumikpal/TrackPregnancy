package com.example.callstatemonitor

import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log

class CustomPhoneStateListener(private val context: Context) : PhoneStateListener() {

    private var isCallActive = false // Track call state
    private var isOutgoingCall = false // Track outgoing call state

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        Log.d("CallState", "Call state changed: $state, Phone number: $phoneNumber")
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                Log.d("CallState", "RINGING state detected")
                if (!isOutgoingCall) {
                    // Incoming call
                    (context as MainActivity).showAlert("RINGING", "Incoming Call")
                    (context as MainActivity).startVibration("RINGING")
                } else {
                    // Outgoing call ringing on the other end
                    (context as MainActivity).showAlert("RINGING", "Outgoing Call Ringing")
                    (context as MainActivity).startVibration("RINGING")
                }
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                Log.d("CallState", "OFFHOOK state detected")
                if (!isCallActive) {
                    // Call is active (connected)
                    (context as MainActivity).showAlert("CONNECTED", "Call Active")
                    isCallActive = true
                    (context as MainActivity).startSpeechRecognition()
                } else {
                    // Another call waiting
                    (context as MainActivity).showAlert("WAITING", "Another Call Waiting")
                    (context as MainActivity).startVibration("WAITING")
                }
            }
            TelephonyManager.CALL_STATE_IDLE -> {
                Log.d("CallState", "IDLE state detected")
                // Call ended
                (context as MainActivity).showAlert("IDLE", "Call Ended")
                isCallActive = false
                isOutgoingCall = false
                (context as MainActivity).resetStatus()
            }
        }
    }

    // Track outgoing call initiation
    fun trackOutgoingCall() {
        isOutgoingCall = true
    }
}