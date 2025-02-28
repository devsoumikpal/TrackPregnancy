package com.example.callstatemonitor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class OutgoingCallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_NEW_OUTGOING_CALL) {
            val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
            Log.d("OutgoingCallReceiver", "Outgoing call to: $phoneNumber")

            if (context is MainActivity) {
                context.trackOutgoingCall()
            }
        }
    }
}