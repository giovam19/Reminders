package com.example.bdreminder.Controller

import android.content.Context
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.example.bdreminder.Model.Reminders
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class FileManager(var context: Context) {
    val filename = "db.json"
    val db = Firebase.firestore

    fun getReminders(callback: (MutableList<Reminders>) -> Unit, failure: (Exception) -> Unit) {
        db.collection("Reminders")
            .orderBy("month", Query.Direction.ASCENDING)
            .orderBy("day",Query.Direction.ASCENDING)
            .get().
            addOnSuccessListener { result ->
            val lista = mutableListOf<Reminders>()
            for (reminder in result) {
                Log.d("Reminder ${reminder.id}", "${reminder.data}")
                val r = Reminders(
                    reminder.id,
                    reminder.data["name"].toString(),
                    reminder.data["description"].toString(),
                    reminder.data["ejectime"].toString(),
                    reminder.getLong("day")!!.toInt(),
                    reminder.getLong("month")!!.toInt(),
                    reminder.getLong("year")!!.toInt(),
                    Reminders.ReminderTypes.valueOf(reminder.data["type"].toString())
                )
                lista.add(r)

                callback(lista)
            }
        }.addOnFailureListener { exception ->
            Log.w("Error getting documents","Error: $exception")
            failure(exception)
        }
    }

    fun addReminder(reminder: Reminders, progressBar: ProgressBar) {
        val obj = hashMapOf(
            "name" to reminder.name,
            "description" to reminder.description,
            "day" to reminder.day,
            "month" to reminder.month,
            "year" to reminder.year,
            "ejectime" to reminder.ejectTime,
            "type" to reminder.type
        )

        progressBar.visibility = ProgressBar.VISIBLE
        db.collection("Reminders").add(obj).addOnSuccessListener { added ->
            Log.d("Reminder added Firebase: ", added.id)

            progressBar.visibility = ProgressBar.GONE
            Toast.makeText(context, "Reminder saved!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Log.w("Error adding Reminder Firebase: ", e)

            progressBar.visibility = ProgressBar.GONE
            Toast.makeText(context, "Fail saving Reminder", Toast.LENGTH_SHORT).show()
        }
    }
}