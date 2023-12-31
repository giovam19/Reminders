package com.example.bdreminder.Controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bdreminder.Model.Reminders
import com.example.bdreminder.R

class ReminderAdapter(private val items: List<Reminders>) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val texto: TextView = itemView.findViewById(R.id.itemName)
        val fecha: TextView = itemView.findViewById(R.id.itemDate)
        val hora: TextView = itemView.findViewById(R.id.itemHour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reminder_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.texto.text = item.name
        holder.fecha.text = "${item.day}/${item.month}"
        holder.hora.text = item.ejectTime
    }

    override fun getItemCount(): Int {
        return items.size
    }
}