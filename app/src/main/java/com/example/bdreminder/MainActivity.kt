package com.example.bdreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
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

    lateinit var taskButton: ImageView
    lateinit var bdButton: ImageView
    lateinit var addButton: ImageView
    lateinit var recyclerView : RecyclerView
    lateinit var adapter : ReminderAdapter
    lateinit var itemDecoration : ReminderItemDecorator
    lateinit var itemTouchHelper: ItemTouchHelper

    lateinit var list : List<Reminders>

    var blueColor : Int = 0
    var blackColor : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        taskButton.setOnClickListener {
            setButtonFocus(taskButton)
        }

        bdButton.setOnClickListener {
            setButtonFocus(bdButton)
        }

        addButton.setOnClickListener {
            setButtonFocus(addButton)

            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        setButtonFocus(taskButton)
    }

    override fun onStart() {
        super.onStart()

        setButtonFocus(taskButton)

        list = getListData()
        adapter = ReminderAdapter(list)
        recyclerView.adapter = adapter

    }

    private fun init() {
        list = getListData()

        adapter = ReminderAdapter(list)
        itemDecoration = ReminderItemDecorator()
        itemTouchHelper = ItemTouchHelper(ReminderTouchHelper(adapter))

        taskButton = findViewById(R.id.tasksB)
        bdButton = findViewById(R.id.bdB)
        addButton = findViewById(R.id.addB)
        recyclerView = findViewById(R.id.reminderList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(itemDecoration)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        blueColor = ContextCompat.getColor(this, R.color.blue)
        blackColor = ContextCompat.getColor(this, R.color.black)
    }

    private fun cleanButtonColor() {
        taskButton.setColorFilter(blackColor)
        bdButton.setColorFilter(blackColor)
        addButton.setColorFilter(blackColor)
    }

    private fun setColorFocus(button: ImageView) {
        button.setColorFilter(blueColor)
    }

    fun setButtonFocus(button: ImageView) {
        cleanButtonColor()
        setColorFocus(button)
    }

    fun getListData(): MutableList<Reminders> {
        val manager = FileManager(this)
        val data = manager.readFromDB()

        return manager.convertToObject(data)
    }

    fun setEventList(): MutableList<Reminders> {
        setButtonFocus(taskButton)

        return list.filter { it.type == Reminders.ReminderTypes.EVENT } as MutableList<Reminders>
    }
}