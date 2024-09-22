package com.example.mad_pra_4

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import android.app.TimePickerDialog
import java.util.Calendar
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var alarmSetCard: View
    private lateinit var alarmSetText: TextView
    private lateinit var cancelAlarmButton: Button
    private lateinit var realTimeClock: TextView
    private lateinit var alarmTimeDisplay: TextView

    private val handler = Handler(Looper.getMainLooper())
    // Updated format to include time and date
    private val timeFormat = SimpleDateFormat("hh:mm:ss a MMM, dd yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createAlarmButton: Button = findViewById(R.id.button)
        alarmSetCard = findViewById(R.id.alarmSetCard)
        alarmSetText = findViewById(R.id.alarmSetText)
        cancelAlarmButton = findViewById(R.id.cancelAlarmButton)
        realTimeClock = findViewById(R.id.realTimeClock)
        alarmTimeDisplay = findViewById(R.id.alarmTimeDisplay)

        alarmSetCard.visibility = View.GONE

        updateClock()

        createAlarmButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }

                setAlarm(calendar.timeInMillis)

                val alarmTimeText = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(calendar.time)
                alarmSetText.text = "Alarm Set time"
                alarmTimeDisplay.text = alarmTimeText
                alarmSetCard.visibility = View.VISIBLE
            }, currentHour, currentMinute, false).show()
        }

        cancelAlarmButton.setOnClickListener {
            cancelAlarm()
            Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show()
            alarmSetCard.visibility = View.GONE
        }
    }

    private fun updateClock() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                realTimeClock.text = timeFormat.format(Date())
                handler.postDelayed(this, 1000)
            }
        }, 10)
    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

    private fun cancelAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }
}
