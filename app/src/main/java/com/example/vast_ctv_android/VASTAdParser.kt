package com.example.vast_ctv_android

import org.xmlpull.v1.XmlPullParser

class VASTAdParser : VASTBaseParser() {
    val ad = Ad()
    override fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser? {
        if (INLINE == elementName) {
            return null
        } else if (WRAPPER == elementName) {
            return null
        }
        return null
    }

    override fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?) {

    }

    companion object {
        private const val INLINE = "InLine"
        private const val WRAPPER = "Wrapper"
    }
}