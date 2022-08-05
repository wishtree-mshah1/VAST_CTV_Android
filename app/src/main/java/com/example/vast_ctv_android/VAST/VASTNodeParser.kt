package com.example.vast_ctv_android.VAST

import org.xmlpull.v1.XmlPullParser

/**
 * Created by Manav Shah on 25/07/22 - 14: 48: 03.
 */

//Interface for getting parsed data and it was extracted with many parsing classes
interface VASTNodeParser {
    fun didStartElement(elementName: String, parser: XmlPullParser?)
    fun didEndElement(elementName: String, value: String?)
}