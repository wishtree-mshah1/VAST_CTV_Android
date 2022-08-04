package com.example.vast_ctv_android.VAST

import android.util.Xml
import com.integralads.omid.iassdk.videoad.vast.VASTResponse
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.ArrayList

/**
 * Created by Manav Shah on 25/07/22 - 14: 48: 03.
 */

class VastParser {
    //Buffer is a temp variable to hold parsed data one by one
    private var buffer: String? = null
    val parser = Xml.newPullParser()

    var index = 0


    @Throws(IOException::class)
    fun parse(xml: String): VASTResponse {
        val inputStream: InputStream = ByteArrayInputStream(xml.toByteArray(Charset.forName("UTF-8")))
        val responseParser = VASTResponseParser()

        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            //called do parse method and after parsing MediaFile the URL was stored in adurl variable and it returns this url to asyncTask and set the url for a video
            doParse(parser,responseParser)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } finally {
            inputStream.close()
        }
        return responseParser.response
    }

    @Throws(XmlPullParserException::class, IOException::class)
     fun doParse(parser: XmlPullParser, responseParser: VASTResponseParser){
        var eventType = parser.eventType
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
                    println(buffer)
//                    response.adsData.add(buffer.toString())
//                    println("response.ads?.adsData")
//
//                    index++
                    //if tag name is MediaFile then set buffer value to url variable
//                    if (tagname == "MediaFile"){
//                        url = buffer.toString()
//                    }
                    buffer = null
                }
                else -> {
                }
            }
            eventType = parser.next()
        }

        //return url vale to parse function
    }
}