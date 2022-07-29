package com.example.vast_ctv_android.VAST

import org.xmlpull.v1.XmlPullParser

/**
 * Created by Manav Shah on 29/07/22 - 16: 57: 41.
 * Email :- manav.shah@wishtreetech.com
 */
/**
 * Below declared class Data is for
 * And Data class have ______ methods for a ______
 * class Data extends class for a
 **/

class Data: VASTBaseParser() {
    override fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?) {
        if (elementName != null){
            println("valuuuue")
        }
    }

    override fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser? {
        TODO("Not yet implemented")
    }
}