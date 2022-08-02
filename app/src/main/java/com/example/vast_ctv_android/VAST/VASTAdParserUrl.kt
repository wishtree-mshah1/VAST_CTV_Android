package com.example.vast_ctv_android.VAST

import org.xmlpull.v1.XmlPullParser

/**
 * Created by Manav Shah on 25/07/22 - 14: 48: 03.
 * Email :- manav.shah@wishtreetech.com
 */

class VASTAdParserUrl() : VASTBaseParser() {

    //For store Metadata
    val myArrayList = ArrayList<String>()
    override fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser? {
        if (MEDIAFILE == elementName) {
            println("URL GOT")
        }
        return null
    }

    override fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?) {
        if (MEDIAFILE == elementName) {
            //To add Mediafile URL into arrayList
            myArrayList.add(value.toString())
        }
    }

    companion object {
        private const val MEDIAFILE = "MediaFile"
    }

}