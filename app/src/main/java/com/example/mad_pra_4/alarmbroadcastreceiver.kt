package com.example.mad_pra_4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Alarm triggered!", Toast.LENGTH_SHORT).show()
        val serviceIntent = Intent(context, AlarmService::class.java)
        context.startService(serviceIntent)
    }
}
