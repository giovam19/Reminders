package com.example.bdreminder

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bdreminder.Controller.ReminderAdapter
import com.example.bdreminder.Controller.ReminderItemDecorator
import com.example.bdreminder.Model.Reminders

class MainActivity : AppCompatActivity() {

    lateinit var taskButton: ImageView
    lateinit var bdButton: ImageView
    lateinit var addButton: ImageView
    lateinit var recyclerView : RecyclerView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lista = Reminders.factory()
        val adapter = ReminderAdapter(lista)
        val itemDecoration = ReminderItemDecorator()

        taskButton = findViewById(R.id.tasksB)
        bdButton = findViewById(R.id.bdB)
        recyclerView = findViewById(R.id.reminderList)
        addButton = findViewById(R.id.addB)

        val color = ContextCompat.getColor(this, R.color.blue)
        taskButton.setColorFilter(color)

        taskButton.setOnClickListener {
            Toast.makeText(this, "Muestra Tasks", Toast.LENGTH_SHORT).show()
        }

        bdButton.setOnClickListener {
            Toast.makeText(this, "Muestra BDs", Toast.LENGTH_SHORT).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(itemDecoration)
    }
}