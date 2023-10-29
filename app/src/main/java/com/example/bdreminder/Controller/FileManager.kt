package com.example.bdreminder.Controller

import android.content.Context
import android.widget.Toast
import com.example.bdreminder.Model.Reminders
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter

class FileManager(var context: Context) {
    val filename = "db.json"

    fun readFromDB(): String {
        try {
            val file = File(context.filesDir, filename)
            val json = StringBuilder()
            val bufferedReader = file.bufferedReader()
            bufferedReader.useLines { lines ->
                lines.forEach {
                    json.append(it)
                }
            }
            return json.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error reading data!", Toast.LENGTH_SHORT).show()
            return ""
        }
    }

    fun writeToDB(reminder: Reminders) {
        val gson = Gson()
        val jsonData = gson.toJson(reminder)
        print(jsonData)

        try {
            val file = File(context.filesDir, filename)
            val actuaData = gson.toJson(readFromDB())

            val fileWriter = FileWriter(file)
            fileWriter.write(actuaData)
            fileWriter.close()
        } catch (e : Exception) {
            Toast.makeText(context, "Error saving new reminder!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}