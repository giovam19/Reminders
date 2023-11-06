package com.example.bdreminder.Controller

import android.content.Context
import android.util.Log
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

            Log.i("Object read: ", json.toString())
            return json.toString()
        } catch (e: Exception) {
            e.message?.let { Log.i("Object read error: ", it) }
            return ""
        }
    }

    fun convertToObject(data: String): MutableList<Reminders> {
        try {
            val lista = Gson().fromJson(data, Array<Reminders>::class.java).toMutableList()

            if (lista.isNotEmpty())
                Log.i("Object converted", lista[0].name)

            return lista
        } catch (e : Exception) {
            e.message?.let { Log.i("Convert json error: ", it) }
            return mutableListOf<Reminders>()
        }
    }

    fun writeToDB(reminder: Reminders, olds: MutableList<Reminders>) {
        val gson = Gson()

        try {
            val file = File(context.filesDir, filename)

            olds.add(reminder)

            val jsonData = gson.toJson(olds)
            val fileWriter = FileWriter(file)
            fileWriter.write(jsonData)
            Log.i("Object saved: ", jsonData)
            fileWriter.close()
        } catch (e : Exception) {
            e.message?.let { Log.i("Object write error: ", it) }
        }
    }
}