package com.cobrapdf.reader.extension

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date
import kotlin.math.abs

object Date {

    fun getCurrentDate(date: Date): String? {
        val sDate: Calendar = toCalendar(Date().time)!!
        val eDate: Calendar = toCalendar(date.time)!!

        val milis1 = sDate.timeInMillis
        val milis2 = eDate.timeInMillis
        val dayDiff = abs((milis2 - milis1) / (24 * 60 * 60 * 1000)).toInt()
        val yearDiff = sDate[Calendar.YEAR] - eDate[Calendar.YEAR]
        Log.d("date", "yeardiff: $yearDiff")
        return if (yearDiff == 0) {
            when (dayDiff) {
                0 -> "Today"
                1 -> "Yesterday"
                else -> {
                    val format = SimpleDateFormat("EEEE, dd MMM", Locale.getDefault())
                    format.format(date)
                }
            }
        } else {
            val format = SimpleDateFormat("EEEE, dd MMM YYYY", Locale.getDefault())
            format.format(date)
        }
    }

    private fun toCalendar(timestamp: Long): Calendar? {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar
    }

    fun getDate(date: Date?): String? {
        val formatter = SimpleDateFormat("dd-mm-yyyy HH:mm",Locale.getDefault())
        return formatter.format(date)
    }
}