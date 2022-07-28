package com.example.vast_ctv_android

import org.xmlpull.v1.XmlPullParser

interface VASTNodeParser {
    fun didStartElement(elementName: String, parser: XmlPullParser?)
    fun didEndElement(elementName: String, value: String?)
}