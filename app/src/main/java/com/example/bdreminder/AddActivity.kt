package com.example.bdreminder

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.bdreminder.Controller.FileManager
import com.example.bdreminder.Model.Reminders
import com.google.gson.Gson
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.Calendar

class AddActivity : AppCompatActivity() {
    lateinit var addButton : Button
    lateinit var cancelButton : Button
    lateinit var nameText: EditText
    lateinit var descriptionText: EditText
    lateinit var datePick : TextView
    lateinit var hourPick : TextView
    lateinit var typePick : Spinner

    var name : String = ""
    var description : String = ""
    var ejectTime: String = ""
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var type : Reminders.ReminderTypes = Reminders.ReminderTypes.EVENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_reminder)

        init()

        addButton.setOnClickListener {
            name = nameText.text.toString()
            description = descriptionText.text.toString()

            var reminder = Reminders(name, description, ejectTime, day, month, year, type)

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
                val horaSeleccionada = "$selectedHour:$selectedMinute"
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

        typePick.adapter = adapter
    }

    private fun saveNewReminder(reminder: Reminders) {
        val manager = FileManager(this)
        val actualData = manager.readFromDB()

        val olds = manager.convertToObject(actualData)
        manager.writeToDB(reminder, olds)

        Toast.makeText(this@AddActivity, "Reminder saved!", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun setNotification(reminder: Reminders) {
        val hours = reminder.ejectTime.split(":")
        val hour = hours[0].toInt()
        val minute = if(reminder.type == Reminders.ReminderTypes.EVENT) hours[1].toInt() else 0

        val calendar = android.icu.util.Calendar.getInstance()
        calendar.set(android.icu.util.Calendar.YEAR, reminder.year)
        calendar.set(android.icu.util.Calendar.MONTH, reminder.month-1)
        calendar.set(android.icu.util.Calendar.DAY_OF_MONTH, reminder.day)
        calendar.set(android.icu.util.Calendar.HOUR_OF_DAY, hour)
        calendar.set(android.icu.util.Calendar.MINUTE, minute)
        calendar.set(android.icu.util.Calendar.SECOND, 0)

        val intent = Intent(this, MyReceiver::class.java)
        val titulo = if (reminder.type == Reminders.ReminderTypes.EVENT) "${reminder.name} 📝" else "${reminder.name} 🎉"
        intent.putExtra("titulo", titulo)
        intent.putExtra("descripcion", reminder.description)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val titulo = intent?.getStringExtra("titulo")
            val descripcion = intent?.getStringExtra("descripcion")

            val notificationId = 1
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationBuilder = NotificationCompat.Builder(context, "channel_id")
                .setContentTitle(titulo)
                .setContentText(descripcion)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            notificationManager.notify(notificationId, notificationBuilder)
        }
    }
}
