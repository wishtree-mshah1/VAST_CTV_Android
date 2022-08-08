package com.example.vast_ctv_android.VAST

import com.integralads.omid.iassdk.videoad.vast.VASTResponse
import org.xmlpull.v1.XmlPullParser

class VASTResponseParser : VASTBaseParser() {
    val response = VASTResponse()
    override fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser? {
        return if (MEDIA_FILE == elementName) {
            VASTLinearParser()
        }
            else {
                null
        }
    }

    override fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?) {
        // add Mediafile URL in VASTResponse's ads array
        response.ads.add(value.toString())
    }

    companion object {
        private const val MEDIA_FILE = "MediaFile"
    }
}