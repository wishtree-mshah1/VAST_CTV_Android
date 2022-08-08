package com.example.vast_ctv_android.VAST

import org.xmlpull.v1.XmlPullParser

/**
 * Created by Manav Shah on 26/07/22 - 14: 48: 03.
 */


abstract class VASTBaseParser : VASTNodeParser {
    private var currentElement: String? = null
    private var currentParser: VASTNodeParser? = null
    override fun didStartElement(elementName: String, parser: XmlPullParser?) {
        //if currentParser variable is not null
        if (currentParser != null) {
            currentParser?.didStartElement(elementName, parser)
        }
        // if null it's create a parser and call abstract function
        else {
            currentParser = createParser(elementName, parser)
            currentElement = if (currentParser != null) elementName else null
        }
    }

    override fun didEndElement(elementName: String, value: String?) {
//        didEndElement(elementName, value, currentParser)
        //for check the currentelement variable have same value as elementName
        if (currentElement != null && currentElement == elementName) {
            didEndElement(elementName, value, currentParser)
            //after one time of parsing the currentParser and currentElement has to be a null for next tag parsing
            currentParser = null
            currentElement = null
        } else if (currentParser != null) {
            currentParser?.didEndElement(elementName, value)
        }
    }

    protected abstract fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser?
    protected abstract fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?)
}