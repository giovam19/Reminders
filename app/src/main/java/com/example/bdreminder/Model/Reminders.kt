package com.example.bdreminder.Model

class Reminders(
    var id: String,
    var name: String,
    var description: String,
    var ejectTime: String,
    var day: Int,
    var month: Int,
    var year: Int,
    var type: ReminderTypes) {
    enum class ReminderTypes {
        BIRTHDAY, EVENT
    }
    companion object {
        fun factory() : List<Reminders> {
            var lista = mutableListOf<Reminders>()

            for (i in 1..15) {
                val t = if(i%2 == 0) ReminderTypes.BIRTHDAY else ReminderTypes.EVENT
                lista.add(Reminders(i.toString(),"Task $i", "descripcion de task $i", "13:00", 19, 5, 2000, t))
            }

            return lista
        }
    }

    fun dataIsValid() : Boolean {
        if (name == "")
            return false

        if (ejectTime == "")
            return false

        if (day.toInt() == 0)
            return false

        if (month == 0)
            return false

        if (year == 0)
            return false

        return true
    }
}