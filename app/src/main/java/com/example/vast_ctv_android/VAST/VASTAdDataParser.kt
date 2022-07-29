package com.example.vast_ctv_android.VAST

import com.example.vast_ctv_android.videoAd.AdData
import org.xmlpull.v1.XmlPullParser

abstract class VASTAdDataParser : VASTBaseParser() {

    abstract val adData: AdData
    override fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser? {
        return if (AD_VERIFICATIONS == elementName) {
            null
        } else null
    }

    override fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?) {
        println("valueeeee")
        println(value)
        if (AD_VERIFICATIONS == elementName) {
            null
        }
    }

    companion object {
        private const val AD_VERIFICATIONS = "AdVerifications"
    }
}