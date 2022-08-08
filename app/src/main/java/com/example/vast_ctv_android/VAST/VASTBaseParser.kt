package com.example.vast_ctv_android.VAST

import org.xmlpull.v1.XmlPullParser

/**
 * Created by Manav Shah on 26/07/22 - 14: 48: 03.
 */


abstract class VASTBaseParser : VASTNodeParser {
    private var currentElement: String? = null
    private var currentParser: VASTNodeParser? = null
    override fun didStartElement(elementName: String, parser: XmlPullParser?) {
        if (currentParser != null) {
            currentParser?.didStartElement(elementName, parser)
        } else {
            currentParser = createParser(elementName, parser)
            currentElement = if (currentParser != null) elementName else null
        }
    }

    override fun didEndElement(elementName: String, value: String?) {
//        didEndElement(elementName, value, currentParser)
        if (currentElement != null && currentElement == elementName) {
            didEndElement(elementName, value, currentParser)
            currentParser = null
            currentElement = null
        } else if (currentParser != null) {
            currentParser?.didEndElement(elementName, value)
        }
    }

    protected abstract fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser?
    protected abstract fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?)
}