package com.example.vast_ctv_android

import org.xmlpull.v1.XmlPullParser

abstract class VASTAdDataParser : VASTBaseParser() {

    override fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser? {
        return if (AD_VERIFICATIONS == elementName) {
            null
        } else null
    }

    override fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?) {
        if (AD_VERIFICATIONS == elementName) {
        }
    }

    companion object {
        private const val AD_VERIFICATIONS = "AdVerifications"
    }
}