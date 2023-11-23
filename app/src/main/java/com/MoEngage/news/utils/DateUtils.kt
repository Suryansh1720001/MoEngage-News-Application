package com.MoEngage.news.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val DISPLAY_DATE_FORMAT = "dd MMM yyyy, HH:mm"

    fun parseDate(dateString: String?, format: String = DEFAULT_DATE_FORMAT): Date? {
        return try {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            dateFormat.parse(dateString ?: "")
        } catch (e: Exception) {
            null
        }
    }

    fun formatDate(date: Date?, format: String = DISPLAY_DATE_FORMAT): String {
        return try {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            dateFormat.format(date ?: Date())
        } catch (e: Exception) {
            ""
        }
    }
}
