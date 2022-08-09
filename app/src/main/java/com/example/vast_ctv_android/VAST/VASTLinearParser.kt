package com.example.vast_ctv_android.VAST


import org.xmlpull.v1.XmlPullParser
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created by Manav Shah on 07/08/22
 */
class VASTLinearParser : VASTAdMediaDataParser() {
    //obj of LinearAdMediaData class who have variables to store value
    val adData = LinearAdMediaData()

    //called override method getAdMediaData from VASTAdMediaDataParser class it returns data which is available in LinearAdMediaData class
    override fun getAdMediaData(): AdMediaData? {
        return adData
    }

    // override from VASTBaseParser and it checks tagname or elementName is MediaFile or not
    override fun didStartElement(elementName: String, parser: XmlPullParser?) {
        if (MEDIA_FILE == elementName) {
            val count = parser?.attributeCount
        }
    }

    // if the tagname is Duration, MediaFile and ClickThrough then sort it and store it.
    override fun didEndElement(elementName: String, value: String?) {
         if (MEDIA_FILE == elementName) {
            adData.videoUrl = value
        } else if (DURATION == elementName) {
            var str = value
             //format the duration data
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