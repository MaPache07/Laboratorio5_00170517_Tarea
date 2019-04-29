package com.mapache.pokedex.utilities

import android.net.Uri
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class NetworkUtil {

    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url: URL): String {
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val `in` = urlConnection.inputStream
            val scanner = Scanner(`in`)
            scanner.useDelimiter("\\A")
            val hasInput = scanner.hasNext()
            return if (hasInput) {
                scanner.next()
            } else {
                ""
            }
        } finally {
            urlConnection.disconnect()
        }
    }

    fun buildUrlSingle(url: String) : URL {
        var uri : Uri = Uri.parse(url).buildUpon().build()
        var u = try {
            URL(uri.toString())
        } catch (e: MalformedURLException) {
            URL("")
        }
        return u
    }
}