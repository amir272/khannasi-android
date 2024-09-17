package com.manipur.khannasi.misc

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration
import java.time.temporal.ChronoUnit


class CustomDateTimeFormatter {

    companion object {
        fun calculateDateTimeDifference(dateTimeString: String): String {
            var difference = ""

            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val dateTime = LocalDateTime.parse(dateTimeString, formatter)
            val now = LocalDateTime.now()
            val years = ChronoUnit.YEARS.between(dateTime, now)
            val months = ChronoUnit.MONTHS.between(dateTime.plusYears(years), now)
            val duration = Duration.between(dateTime, now)

            val days = duration.toDays()
            val hours = duration.toHours() % 24
            val minutes = duration.toMinutes() % 60
            val seconds = duration.seconds % 60

            println("Time difference: $years years, $months months, $days days, $hours hours, $minutes minutes, $seconds seconds ago")

            difference = if(years > 0) {
                "$years years ago"
            } else if(months > 0) {
                "$months months ago"
            } else if(days > 0) {
                "$days days ago"
            } else if(hours > 0) {
                "$hours hours ago"
            } else if(minutes > 0) {
                "$minutes minutes ago"
            } else {
                "$seconds seconds ago"
            }
            return difference
        }
    }
}