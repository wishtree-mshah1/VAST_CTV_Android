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
 * Created by Manav Shah on 05/08/22 - 15: 34: 31.
 */



class VideoLoader() {
    interface VideoAdLoaderListener {
        fun loadedVASTResponse(reponse: VASTResponse?)
        fun failedLoadVASTResponse()
    }
    var mRequestQueue: RequestQueue? = null
    var mStringRequest: StringRequest? = null
    private var buffer: String? = null
    private var listener: VideoAdLoaderListener? = null

    fun loadUrl(url: String, homeActivity: VASTActivity, listener: VideoAdLoaderListener?){
        this.listener = listener
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
                    println(b)

                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }

            }) { error -> Log.i("Error", "Error :$error") }

        mRequestQueue!!.add(mStringRequest)
    }
    private fun handleVASTResponse(response: VASTResponse?) {
        if (response != null) {
            listener?.loadedVASTResponse(response)
        }
    }

    // This block of code perform Async Task it parse the url to parser and get the video URL after parsing and set it as a Video URL
    private inner class VASTParserAsyncTask : AsyncTask<String?, Void?, VASTResponse?>() {
        protected override fun doInBackground(vararg params: String?): VASTResponse? {
            return try {
                // send URL to parse function
                params[0]?.let { parse(it) }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(response: VASTResponse?) {
            //call handleVASTResponse for get and handle the response
            handleVASTResponse(response)

        }
    }

    @Throws(IOException::class)
    fun parse(xml: String): VASTResponse {
        val inputStream: InputStream =
            ByteArrayInputStream(xml.toByteArray(Charset.forName("UTF-8")))
        val responseParser = VASTResponseParser()

        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            //called do parse method and after parsing MediaFile the URL was stored in adurl variable and it returns this url to asyncTask and set the url for a video
            doParse(parser,responseParser)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } finally {
            inputStream.close()
        }
        return responseParser.response
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun doParse(parser: XmlPullParser,responseParser: VASTResponseParser) {
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            //tagname is for store a tags name temporary
                val tagname = parser.name

            when (eventType) {

                XmlPullParser.START_TAG -> {
                    buffer = null
                    responseParser.didStartElement(tagname, parser)
                }
                XmlPullParser.TEXT -> buffer = parser.text.trim { it <= ' ' }
                XmlPullParser.END_TAG -> {
                    //one by one get value with there tag name
                    responseParser.didEndElement(tagname, buffer!!)
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

}