package com.example.vast_ctv_android.VAST

import org.xmlpull.v1.XmlPullParser

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