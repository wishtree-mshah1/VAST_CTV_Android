package com.example.vast_ctv_android.VAST

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

/**
 * Created by Manav Shah on 25/07/22 - 14: 48: 03.
 * Email :- manav.shah@wishtreetech.com
 */

class VastParser {
    //Buffer is a temp variable to hold parsed data one by one
    private var buffer: String? = null
    val parser = Xml.newPullParser()


    @Throws(IOException::class)
    fun parse(xml: String): String? {
        val inputStream: InputStream = ByteArrayInputStream(xml.toByteArray(Charset.forName("UTF-8")))
        val responseParser = VASTResponseParser()
        var adurl: String = ""
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            //called do parse method and after parsing MediaFile the URL was stored in adurl variable and it returns this url to asyncTask and set the url for a video
             adurl = doParse(parser,responseParser)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } finally {
            inputStream.close()
        }
        return adurl
    }

    @Throws(XmlPullParserException::class, IOException::class)
     fun doParse(parser: XmlPullParser, responseParser: VASTResponseParser): String {
        var eventType = parser.eventType
        var url: String =""
        while (eventType != XmlPullParser.END_DOCUMENT) {
            //tagname is for store a tags name temporary
            val tagname = parser.name

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    buffer = null
                    responseParser.didStartElement(tagname, parser)
                }
                XmlPullParser.TEXT -> buffer = parser.text.trim { it <= ' ' }
                XmlPullParser.END_TAG -> {
                    //one by one get value with there tag name
                    responseParser.didEndElement(tagname, buffer!!)
                    //if tag name is MediaFile then set buffer value to url variable
                    if (tagname == "MediaFile"){
                        url = buffer.toString()
                    }
                    buffer = null
                }
                else -> {
                }
            }
            eventType = parser.next()
        }
        //return url vale to parse function
        return url
    }
}