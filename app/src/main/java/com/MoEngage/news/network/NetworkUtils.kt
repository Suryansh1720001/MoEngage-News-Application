package com.MoEngage.news.network

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NetworkUtils {

    private const val TAG = "NetworkUtils"

    fun fetchNewsArticles(apiUrl: String): String? {
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null

        try {
            val url = URL(apiUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            // Set connection timeout and read timeout if needed
            connection.connectTimeout = 10000 // 10 seconds
            connection.readTimeout = 10000 // 10 seconds

            connection.connect()

            val inputStream = connection.inputStream
            val buffer = StringBuffer()

            if (inputStream != null) {
                reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    buffer.append(line).append("\n")
                }
            }

            return buffer.toString()

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching data: ${e.message}")
            return null
        } finally {
            connection?.disconnect()
            try {
                reader?.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error closing reader: ${e.message}")
            }
        }
    }
}
