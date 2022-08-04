package com.example.vast_ctv_android.VAST

import com.integralads.omid.iassdk.videoad.vast.VASTResponse
import org.xmlpull.v1.XmlPullParser

/**
 * Created by Manav Shah on 28/07/22 - 14: 48: 03.
 */

class VASTResponseParser() : VASTBaseParser() {
    val response = VASTResponse()
    override fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser? {
        return if (AD == elementName) {
            VASTAdParserUrl()
        } else null
    }

    override fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?) {
        println("valueeeeee")
        println(value)
    }

    companion object {
        private const val AD = "Ad"
    }
}