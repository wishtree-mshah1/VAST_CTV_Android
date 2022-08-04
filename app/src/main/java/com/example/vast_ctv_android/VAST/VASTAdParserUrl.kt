package com.example.vast_ctv_android.VAST

import org.xmlpull.v1.XmlPullParser

/**
 * Created by Manav Shah on 25/07/22 - 14: 48: 03.
 */

class VASTAdParserUrl() : VASTBaseParser(), LinearAdMediaData {

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
            AdMediaData(value.toString())
        }

    }

    companion object {
        private const val MEDIAFILE = "MediaFile"
    }

    override var videoUrll: String?
        get() = TODO("Not yet implemented")
        set(value) {}

}