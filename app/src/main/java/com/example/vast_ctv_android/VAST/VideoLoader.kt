package com.example.vast_ctv_android.VAST

import android.os.AsyncTask
import android.util.Xml
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.exoplayer2.util.Log
import com.integralads.omid.iassdk.videoad.vast.VASTResponse
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

/**
 * Below declared class VastParsing is for
 * And VastParsing class have ______ methods for a ______
 * class VastParsing extends class for a
 **/


/**
 * Created by Manav Shah on 05/08/22 - 15: 34: 31.
 */

interface videoData{
    fun urlData(urlData: String)
}


class VideoLoader(): VASTBaseParser(),videoData {

    var mRequestQueue: RequestQueue? = null
    var mStringRequest: StringRequest? = null
    private var buffer: String? = null
    val response = VASTResponse()
    var url: String = ""

    fun loadUrl(url: String, homeActivity: VASTActivity){
        sendAndRequestResponse(url,homeActivity)

    }

    fun sendAndRequestResponse(
        DEFAULT_NATIVE_VIDEO_AD_URL: String,
        homeActivity: VASTActivity
    ) {
        //RequestQueue initialized

        mRequestQueue = Volley.newRequestQueue(homeActivity)

        //String Request initialized
        mStringRequest = StringRequest(
            Request.Method.GET, DEFAULT_NATIVE_VIDEO_AD_URL,
            { response ->

                try {
                    val a = parse(DEFAULT_NATIVE_VIDEO_AD_URL)
                    val b = VASTParserAsyncTask()
                    b.execute(response)

                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }

            }) { error -> Log.i("Error", "Error :$error") }

        mRequestQueue!!.add(mStringRequest)
    }

    // This block of code perform Async Task it parse the url to parser and get the video URL after parsing and set it as a Video URL
    private inner class VASTParserAsyncTask : AsyncTask<String?, Void?, VASTResponse?>() {
        protected override fun doInBackground(vararg params: String?): VASTResponse? {
            return try {
                params[0]?.let { parse(it) }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(response: VASTResponse?) {

        }
    }

    @Throws(IOException::class)
    fun parse(xml: String): VASTResponse {
        val inputStream: InputStream =
            ByteArrayInputStream(xml.toByteArray(Charset.forName("UTF-8")))

        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            //called do parse method and after parsing MediaFile the URL was stored in adurl variable and it returns this url to asyncTask and set the url for a video
            doParse(parser)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } finally {
            inputStream.close()
        }
        return response
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun doParse(parser: XmlPullParser) {
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            //tagname is for store a tags name temporary
            val tagname = parser.name

            when (eventType) {

                XmlPullParser.START_TAG -> {
                    buffer = null
                    didStartElement(tagname, parser)
                }
                XmlPullParser.TEXT -> buffer = parser.text.trim { it <= ' ' }
                XmlPullParser.END_TAG -> {
                    //one by one get value with there tag name
                    didEndElement(tagname, buffer!!)
                    println(buffer)
                    if (tagname == "MediaFile") {

                    }

                    buffer = null
                }
                else -> {
                }
            }
            eventType = parser.next()
        }

    }

    override fun createParser(elementName: String, parser: XmlPullParser?): VASTNodeParser? {
        if (VideoLoader.MEDIAFILE == elementName) {
            println("URL GOT")
        }
        return null
    }

    override fun urlData(urlData: String) {
        println(urlData)
        url = urlData
        dataArary()
    }


    override fun didEndElement(elementName: String, value: String?, parser: VASTNodeParser?) {
        if (VideoLoader.MEDIAFILE == elementName) {
            urlData(value.toString())
            println("eeeeeeee")

        }

    }

    companion object {
        private const val MEDIAFILE = "MediaFile"
    }

}