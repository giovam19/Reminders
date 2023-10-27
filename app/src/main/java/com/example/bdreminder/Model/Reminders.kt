package com.example.bdreminder.Model

import java.util.Date

class Reminders (
    var id: Int,
    var name: String,
    var description: String,
    var ejectTime: Int,
    var date: Date,
    var type: ReminderTypes) {

    enum class ReminderTypes {
        BIRTHDAY, EVENT
    }
    companion object {
        fun factory() : List<Reminders> {
            var lista = mutableListOf<Reminders>()

            for (i in 1..15) {
                val t = if(i%2 == 0) ReminderTypes.BIRTHDAY else ReminderTypes.EVENT
                lista.add(Reminders(i, "Task $i", "descripcion de task $i", 1, Date(), t))
            }

            return lista
        }
    }
}