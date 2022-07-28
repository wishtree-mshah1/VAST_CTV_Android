package com.example.vast_ctv_android

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

abstract class VastParser: VASTNodeParser {
    private var currentElement: String? = null
    private var currentParser: VASTNodeParser? = null
    private var buffer: String? = null
    val parser = Xml.newPullParser()
    @Throws(IOException::class)
    fun parse(xml: String) {
        val inputStream: InputStream = ByteArrayInputStream(xml.toByteArray(Charset.forName("UTF-8")))
//        val responseParser = VASTResponseParser()
        try {

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            doParse(parser)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } finally {
            inputStream.close()
        }


    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun doParse(parser: XmlPullParser) {
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagname = parser.name
            println("tagname")
            println(tagname)
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    buffer = null
                    didStartElement("MediaFile", parser)
                }
                XmlPullParser.TEXT -> buffer = parser.text.trim { it <= ' ' }
                XmlPullParser.END_TAG -> {
                    didEndElement("MediaFile", buffer!!)
                    buffer = null
                }
                else -> {
                }
            }
            eventType = parser.next()
        }
    }


    override fun didStartElement(elementName: String, parser: XmlPullParser?) {
        if (currentParser != null) {
            currentParser?.didStartElement(elementName, parser)
        } else {
            currentParser = createParser(elementName, parser)
            currentElement = if (currentParser != null) elementName else null
        }
    }

    override fun didEndElement(elementName: String, value: String?) {
        if (currentElement != null && currentElement == elementName) {
            didEndElement(elementName, value, currentParser)
            currentParser = null
            currentElement = null
        } else if (currentParser != null) {
            currentParser?.didEndElement(elementName, value)
        }
    }
    protected abstract fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser?
    protected abstract fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?)
}