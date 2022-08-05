package com.example.vast_ctv_android.VAST

import org.xmlpull.v1.XmlPullParser

/**
 * Created by Manav Shah on 25/07/22 - 14: 48: 03.
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
    override fun urlData(urlData: String) {
        println(urlData)
    }
    companion object {
        private const val MEDIAFILE = "MediaFile"
    }



}