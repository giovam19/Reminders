package com.example.bdreminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var taskButton: Button = findViewById(R.id.tasks)
    var bdButton: Button = findViewById(R.id.BDs)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskButton.setOnClickListener {
            Toast.makeText(this, "Muestra Tasks", Toast.LENGTH_SHORT).show()
        }

        bdButton.setOnClickListener {
            Toast.makeText(this, "Muestra BDs", Toast.LENGTH_SHORT).show()
        }
    }
}