package com.example.vast_ctv_android.VAST

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class VastParser {
    private var currentElement: String? = null
    private var currentParser: VASTNodeParser? = null
    private var buffer: String? = null
    val parser = Xml.newPullParser()
    var temp = null

    @Throws(IOException::class)
    fun parse(xml: String) {
        val inputStream: InputStream = ByteArrayInputStream(xml.toByteArray(Charset.forName("UTF-8")))
        val responseParser = VASTResponseParser()
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            doParse(parser,responseParser)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } finally {
            inputStream.close()
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
     fun doParse(parser: XmlPullParser, responseParser: VASTResponseParser) {
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagname = parser.name
            val item = parser.text
            println(item)
            println("tagname")
            println(tagname)
            println(eventType)
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    buffer = null
                    responseParser.didStartElement(tagname, parser)
                }
                XmlPullParser.TEXT -> buffer = parser.text.trim { it <= ' ' }
                XmlPullParser.END_TAG -> {
                    responseParser.didEndElement(tagname, buffer!!)
                    buffer = null
                    var a = Data().temp
                    println("aaaaaaaa")
                    println(a)
                }
                else -> {
                }
            }

            eventType = parser.next()
        }
    }
}