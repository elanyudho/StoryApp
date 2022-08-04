package com.elanyudho.storyapp.utils.extensions

import android.util.Log
import android.util.Patterns
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.convertDate(): String {

    var convTime = ""

    val suffix = "Ago"


    try {
        //change to local time
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val localTime: String = LocalDateTime.parse(this, formatter)
            .atOffset(ZoneOffset.UTC)
            .atZoneSameInstant(ZoneId.of("Asia/Jakarta"))
            .format(formatter)


        //change to time ago format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val pasTime = dateFormat.parse(localTime)
        val nowTime = Date()
        var dateDiff =  nowTime.time - pasTime.time

        val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
        when {
            second < 60 -> {
                convTime = "Today"
            }
            minute < 60 -> {
                convTime = "Today"
            }
            hour < 24 -> {
                convTime = "Today"
            }
            day >= 7 -> {
                convTime = if (day > 360) {
                    (day / 360).toString() + " Years " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " Months " + suffix
                } else {
                    (day / 7).toString() + " Week " + suffix
                }
            }
            day < 7 -> {
                convTime = "$day Days $suffix"
            }
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        Log.e("ConvTimeE", e.message.toString())
    }

    return convTime
}