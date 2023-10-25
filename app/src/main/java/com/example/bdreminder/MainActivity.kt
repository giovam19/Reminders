package com.example.bdreminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

class MainActivity : AppCompatActivity() {

    lateinit var taskButton: Button
    lateinit var bdButton: Button
    lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lista = Reminders.factory()

        taskButton = findViewById(R.id.tasks)
        bdButton = findViewById(R.id.bd)

        taskButton.setOnClickListener {
            Toast.makeText(this, "Muestra Tasks", Toast.LENGTH_SHORT).show()
        }

        bdButton.setOnClickListener {
            Toast.makeText(this, "Muestra BDs", Toast.LENGTH_SHORT).show()
        }

        recyclerView = findViewById(R.id.reminderList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = ReminderAdapter(lista)
        val itemDecoration = ReminderItemDecorator()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(itemDecoration)
    }
}