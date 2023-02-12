package ru.geekbrains.mymaterialproject.util.date

import java.text.SimpleDateFormat
import java.util.*

private const val MILLIS_IN_DAY = 86400000

fun Date.toString(format: String = "yyyy-MM-dd", locale: Locale = Locale.US): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

//TODO : Аннотации для группы констант
const val TODAY = "today"
const val YESTERDAY = "yesterday"
const val BEFORE_YESTERDAY = "beforeYesterday"

fun getCurrentDateTime(day: String): Date {
    val time = Calendar.getInstance().time
    when (day) {
        TODAY -> {}
        YESTERDAY -> {
            time.time = (time.time - MILLIS_IN_DAY)
        }
        BEFORE_YESTERDAY -> {
            time.time = (time.time - (2 * MILLIS_IN_DAY))
        }
    }
    return time
}