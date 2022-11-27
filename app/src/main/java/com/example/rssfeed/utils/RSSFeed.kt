package com.example.rssfeed.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.Xml
import android.widget.Toast
import com.example.rssfeed.database.RssFeedItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

private val ns: String? = null

class RSSFeed(){

     @Throws(XmlPullParserException::class, IOException::class)
     private fun parse (inputStream: InputStream): List<RssFeedItem>{
         inputStream.use { inputStream ->
             val parser: XmlPullParser = Xml.newPullParser()
             parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
             parser.setInput(inputStream, null)
             parser.nextTag()
             return readFeed(parser)
         }
     }

     @Throws(XmlPullParserException::class, IOException::class)
     private fun readFeed(parser: XmlPullParser): List<RssFeedItem> {
         val entries = mutableListOf<RssFeedItem>()
         parser.nextTag()
         parser.require(XmlPullParser.START_TAG, ns, "channel")
         while (parser.next() != XmlPullParser.END_TAG) {
             if (parser.eventType != XmlPullParser.START_TAG) {
                 continue
             }
             if (parser.name == "item") {
                 entries.add(readEntry(parser))
             } else {
                 skip(parser)
             }
         }
         return entries
     }

     @Throws(XmlPullParserException::class, IOException::class)
     private fun readEntry(parser: XmlPullParser): RssFeedItem {
         parser.require(XmlPullParser.START_TAG, ns, "item")
         var title: String? = null
         var description: String? = null
         val enclosure = arrayListOf<String>()

         while (parser.next() != XmlPullParser.END_TAG) {
             if (parser.eventType != XmlPullParser.START_TAG) {
                 continue
             }
             when (parser.name) {
                 "title" -> title = readTitle(parser)
                 "description" -> description = readDescription(parser)
                 "enclosure" -> {
                     var link = readLink(parser)
                     enclosure.add(link)
                 }
                 else -> skip(parser)
             }

         }
         return RssFeedItem(0,title, description, enclosure[0])
     }

     @Throws(IOException::class, XmlPullParserException::class)
     private fun readTitle(parser: XmlPullParser): String {
         parser.require(XmlPullParser.START_TAG, ns, "title")
         val title = readText(parser)
         parser.require(XmlPullParser.END_TAG, ns, "title")
         return title
     }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readDescription(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "description")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "description")
        return title
    }

     @Throws(IOException::class, XmlPullParserException::class)
     private fun readLink(parser: XmlPullParser): String {
         var link = ""
         parser.require(XmlPullParser.START_TAG, ns, "enclosure")
         val tag = parser.name
         if (tag == "enclosure") {
             link = parser.getAttributeValue(null, "url")
             parser.nextTag()
         }
         return link
     }

     @Throws(IOException::class, XmlPullParserException::class)
     private fun readText(parser: XmlPullParser): String {
         var result = ""
         if (parser.next() == XmlPullParser.TEXT) {
             result = parser.text
             parser.nextTag()
         }
         return result
     }

     @Throws(XmlPullParserException::class, IOException::class)
     private fun skip(parser: XmlPullParser) {
         if (parser.eventType != XmlPullParser.START_TAG) {
             throw IllegalStateException()
         }
         var depth = 1
         while (depth != 0) {
             when (parser.next()) {
                 XmlPullParser.END_TAG -> {
                     depth--
                 }
                 XmlPullParser.START_TAG -> {
                     depth++
                 }
             }
         }
     }

    @Throws(XmlPullParserException::class, IOException::class)
    fun loadXmlFromNetwork(urlString: String): List<RssFeedItem>? {

        val entries: List<RssFeedItem>? = downloadUrl(urlString)?.use { stream ->
            parse(stream)
        }
        Log.d("entries", entries.toString())
        return entries
    }

    @Throws(IOException::class)
    private fun downloadUrl(urlString: String): InputStream? {
        val url = URL(urlString)
        return (url.openConnection() as? HttpURLConnection)?.run {
            readTimeout = 10000
            connectTimeout = 15000
            requestMethod = "GET"
            doInput = true
            connect()
            inputStream
        }
    }

}