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

    fun dataIsValid() : Boolean {
        if (name == "")
            return false

        if (ejectTime == "")
            return false

        if (day == 0)
            return false

        if (month == 0)
            return false

        if (year == 0)
            return false

        return true
    }
}