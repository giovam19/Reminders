package com.example.bdreminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.bdreminder.Controller.FileManager
import com.example.bdreminder.Controller.NotificationReceiver
import com.example.bdreminder.Model.Reminders
import java.util.Calendar

class AddActivity : AppCompatActivity() {
    lateinit var addButton : Button
    lateinit var cancelButton : Button
    lateinit var nameText: EditText
    lateinit var descriptionText: EditText
    lateinit var datePick : TextView
    lateinit var hourPick : TextView
    lateinit var typePick : Spinner
    lateinit var progressBar: ProgressBar

    var name : String = ""
    var description : String = ""
    var ejectTime: String = ""
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var type : Reminders.ReminderTypes = Reminders.ReminderTypes.EVENT

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_reminder)

        init()

        addButton.setOnClickListener {
            name = nameText.text.toString()
            description = descriptionText.text.toString()

            var reminder = Reminders("", name, description, ejectTime, day, month, year, type)

            if (reminder.dataIsValid()) {
                saveNewReminder(reminder)
            } else {
                Toast.makeText(this@AddActivity, "Data Invalid", Toast.LENGTH_SHORT).show()
            }

        }

        cancelButton.setOnClickListener {
            finish()
        }

        datePick.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                val fechaSeleccionada = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                datePick.text = fechaSeleccionada

                this.day = selectedDay
                this.month = selectedMonth+1
                this.year = selectedYear
            }, year, month, day)

            datePickerDialog.show()
        }

        hourPick.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, selectedHour, selectedMinute ->
                val minuteText = if (selectedMinute < 10) "0$selectedMinute" else selectedMinute.toString()
                val horaSeleccionada = "$selectedHour:$minuteText"
                hourPick.text = horaSeleccionada

                ejectTime = horaSeleccionada
            }, hour, minute, true) // El último argumento es para mostrar el reloj de 24 horas

            timePickerDialog.show()
        }

        typePick.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val opcionSeleccionada = parent?.getItemAtPosition(position).toString()
                type = if (opcionSeleccionada == "Event") Reminders.ReminderTypes.EVENT else Reminders.ReminderTypes.BIRTHDAY
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejo de eventos cuando no se ha seleccionado ninguna opción
            }
        }
    }

    private fun init() {
        val adapter = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        addButton = findViewById(R.id.new_add_button)
        cancelButton = findViewById(R.id.new_cancel_add)
        nameText = findViewById(R.id.titleField)
        descriptionText = findViewById(R.id.descriptionField)
        datePick = findViewById(R.id.date_picker)
        hourPick = findViewById(R.id.hour_picker)
        typePick = findViewById(R.id.type_selector)
        progressBar = findViewById(R.id.addProgressBar)

        progressBar.visibility = ProgressBar.GONE
        typePick.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun saveNewReminder(reminder: Reminders) {
        val manager = FileManager(this)

        progressBar.visibility = ProgressBar.VISIBLE

        manager.addReminder(reminder, {
            progressBar.visibility = ProgressBar.GONE
            Toast.makeText(this, "Reminder saved!", Toast.LENGTH_SHORT).show()
            setNotification(reminder)

            Log.d("Notification", "Notification setted")
        },
        { exception ->
            progressBar.visibility = ProgressBar.GONE
            Toast.makeText(this, "Fail saving Reminder", Toast.LENGTH_SHORT).show()
        })

        finish()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun setNotification(reminder: Reminders) {
        val hours = reminder.ejectTime.split(":")
        val hour = hours[0].toInt()
        val minute = hours[1].toInt()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, reminder.year)
        calendar.set(Calendar.MONTH, reminder.month-1)
        calendar.set(Calendar.DAY_OF_MONTH, reminder.day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        Log.i("Notification info", "Day: ${reminder.day}, month: ${reminder.month-1}, year: ${reminder.year}, hour: $hour, minute: $minute")

        val titulo = if (reminder.type == Reminders.ReminderTypes.EVENT) "${reminder.name} 📝" else "${reminder.name} 🎉"
        val intent = Intent(this, NotificationReceiver::class.java)

        val activityIntent = Intent(this, MainActivity::class.java)
        val activityPending = PendingIntent.getActivity(
            this,
            0,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        intent.putExtra("title", titulo)
        intent.putExtra("description", reminder.description)
        intent.putExtra("pending_intent", activityPending)

        setNotificationForExactTime(this, calendar, intent)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun setNotificationForExactTime(context: Context, calendar: Calendar, intent: Intent) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            Log.d("Exact Notification", "Notification setted corrected")
        } catch (e: SecurityException) {
            val permissionIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(permissionIntent)

            Log.w("Exact Notification", "Notification failed setting")
        }
    }
}
