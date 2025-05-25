package com.pwr.bookPlatform.data.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateUtils {

    fun formatDateTime(dateTimeString: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            parser.timeZone = TimeZone.getTimeZone("UTC")
            val date = parser.parse(dateTimeString)

            val formatter = SimpleDateFormat("d MMM HH:mm", Locale.getDefault())

            date?.let { formatter.format(it) } ?: dateTimeString
        } catch (e: Exception) {
            "??:??"
        }
    }
}

