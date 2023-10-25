package com.example.bdreminder

import java.util.Date

class Reminders(var name: String, var description: String, var ejectTime: Int, var date: Date, var always: Boolean) {
    fun showData() {
        println(
            this.name + "\n" +
            this.description + "\n" +
            this.ejectTime + "\n" +
            this.date + "\n" +
            this.always
        )
    }

    companion object {
        fun factory() : List<Reminders> {
            var lista = mutableListOf<Reminders>()

            for (i in 1..15) {
                lista.add(Reminders("Task $i", "descripcion de task $i", 1, Date(), (i%2 == 0)))
            }

            return lista
        }
    }
}