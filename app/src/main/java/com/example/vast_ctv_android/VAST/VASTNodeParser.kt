package com.example.vast_ctv_android.VAST

import org.xmlpull.v1.XmlPullParser

interface VASTNodeParser {
    fun didStartElement(elementName: String, parser: XmlPullParser?)
    fun didEndElement(elementName: String, value: String?)
}