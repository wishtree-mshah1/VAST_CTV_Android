package com.example.vast_ctv_android.VAST

import com.integralads.omid.iassdk.videoad.vast.VASTResponse
import org.xmlpull.v1.XmlPullParser
import java.text.ParseException
import java.text.SimpleDateFormat

class VASTLinearParser : VASTAdMediaDataParser() {
    val adData = LinearAdMediaData()

    override fun getAdMediaData(): AdMediaData? {
        return adData
    }

    override fun didStartElement(elementName: String, parser: XmlPullParser?) {
        if (MEDIA_FILE == elementName) {
            val count = parser?.attributeCount
        }
    }

    override fun didEndElement(elementName: String, value: String?) {
         if (MEDIA_FILE == elementName) {
            adData.videoUrl = value
        } else if (DURATION == elementName) {
            var str = value
            if (str?.contains(".") != true) {
                str = "$str.000"
            }
            val format = SimpleDateFormat("HH:mm:ss.SSS")
            try {
                val date = format.parse(str)
                val date1 = format.parse("00:00:00.000")
                adData.duration = date.time - date1.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        } else if (ClickThrough == elementName) {
            adData.clickThroughUrl = value
        }
    }

    override fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser? {
        return null
    }

    override fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?) {}

    companion object {
        private const val DURATION = "Duration"
        private const val MEDIA_FILE = "MediaFile"
        private const val ClickThrough = "ClickThrough"
    }
}