package com.example.vast_ctv_android

import org.xmlpull.v1.XmlPullParser

class VASTResponseParser : VASTBaseParser() {
    val response = VASTResponse()
    override fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser? {
        return if (AD == elementName) {
            null
        } else null
    }

    override fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?) {
    }

    companion object {
        private const val AD = "Ad"
    }
}