package com.example.vast_ctv_android.VAST

import com.example.vast_ctv_android.videoAd.Ad
import org.xmlpull.v1.XmlPullParser

class VASTAdParserUrl() : VASTBaseParser() {

    val myArrayList = ArrayList<String>()

    override fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser? {
        if (MEDIAFILE == elementName) {
            println("yeee mil gaya")
        }
        return null
    }

    override fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?) {
        if (MEDIAFILE == elementName) {
            println("eeeeeeeeeeeeee")
            println(value)
            myArrayList.add(value.toString())
        }
    }

    companion object {
        private const val MEDIAFILE = "MediaFile"
    }
}