package com.example.vast_ctv_android

import org.xmlpull.v1.XmlPullParser

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
        if (currentElement != null && currentElement == elementName) {
            didEndElement(elementName, value, currentParser)
            currentParser = null
            currentElement = null
        } else if (currentParser != null) {
            currentParser?.didEndElement(elementName, value)
        }
    }

    protected fun readIntAttr(parser: XmlPullParser, attrName: String, index: Int): Int? {
        return if (parser.getAttributeName(index) == attrName) {
            parser.getAttributeValue(index).toInt()
        } else null
    }

    protected abstract fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser?
    protected abstract fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?)
}