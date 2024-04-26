package com.example.bdreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bdreminder.Controller.FileManager
import com.example.bdreminder.Controller.ReminderAdapter
import com.example.bdreminder.Controller.ReminderItemDecorator
import com.example.bdreminder.Controller.ReminderTouchHelper
import com.example.bdreminder.Model.Reminders

class MainActivity : AppCompatActivity() {

    lateinit var eventButton: ImageView
    lateinit var birthButton: ImageView
    lateinit var addButton: ImageView
    lateinit var recyclerView : RecyclerView
    lateinit var adapter : ReminderAdapter
    lateinit var itemDecoration : ReminderItemDecorator
    lateinit var itemTouchHelper: ItemTouchHelper
    lateinit var progressBar: ProgressBar

    lateinit var list : List<Reminders>

    var actualType = Reminders.ReminderTypes.EVENT
    lateinit var actualButton : ImageView

    var blueColor : Int = 0
    var blackColor : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        actualButton = eventButton

        FileManager(this).getReminders({ lista ->
            progressBar.visibility = ProgressBar.GONE
            list = lista
            setList()
            initList()
            R.drawable.delete_item
        },
        { exception ->
            progressBar.visibility = ProgressBar.GONE
            Toast.makeText(this, "Error obteniendo lista de Recordatorios.", Toast.LENGTH_SHORT).show()
        })

        eventButton.setOnClickListener {
            actualType = Reminders.ReminderTypes.EVENT
            actualButton = eventButton
            setList()
        }

        birthButton.setOnClickListener {
            actualType = Reminders.ReminderTypes.BIRTHDAY
            actualButton = birthButton
            setList()
        }

        addButton.setOnClickListener {
            setButtonFocus(addButton)

            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        progressBar.visibility = ProgressBar.VISIBLE
        FileManager(this).getReminders({ lista ->
            progressBar.visibility = ProgressBar.GONE
            list = lista
            setList()
        },
        { exception ->
            progressBar.visibility = ProgressBar.GONE
            Toast.makeText(this, "Error obteniendo lista de Recordatorios.", Toast.LENGTH_SHORT).show()
        })
    }

    private fun init() {
        eventButton = findViewById(R.id.tasksB)
        birthButton = findViewById(R.id.bdB)
        addButton = findViewById(R.id.addB)
        recyclerView = findViewById(R.id.reminderList)
        progressBar = findViewById(R.id.progressList)

        blueColor = ContextCompat.getColor(this, R.color.blue)
        blackColor = ContextCompat.getColor(this, R.color.black)
    }

    private fun initList() {
        itemDecoration = ReminderItemDecorator()
        itemTouchHelper = ItemTouchHelper(ReminderTouchHelper(adapter,this))

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(itemDecoration)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun cleanButtonColor() {
        eventButton.setColorFilter(blackColor)
        birthButton.setColorFilter(blackColor)
        addButton.setColorFilter(blackColor)
    }

    private fun setColorFocus(button: ImageView) {
        button.setColorFilter(blueColor)
    }

    fun setButtonFocus(button: ImageView) {
        cleanButtonColor()
        setColorFocus(button)
    }

    fun setList() {
        setButtonFocus(actualButton)

        val lista = list.filter { it.type == actualType }

        adapter = ReminderAdapter(lista)
        recyclerView.adapter = adapter
    }
}