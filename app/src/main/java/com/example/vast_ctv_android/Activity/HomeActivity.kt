package com.example.vast_ctv_android.Activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vast_ctv_android.R
import com.example.vast_ctv_android.VAST.VASTActivity
import com.example.vast_ctv_android.VAST.VastParser
import com.google.android.exoplayer2.util.Log
import com.google.android.material.textfield.TextInputEditText
import com.integralads.omid.iassdk.videoad.vast.VASTResponse
import java.io.IOException

/**
 * Created by Manav Shah on 20/07/2022.
 */

class HomeActivity : AppCompatActivity() {
    lateinit var  applyButton: Button
    lateinit var  loadButton: Button
    lateinit var  urlEdt: TextInputEditText

    val HOSTNAME_URL = "http://mobile-static.adsafeprotected.com"
    val CREATIVE_VERSION = "omid-v1/certification-v013"
    val VERSIONED_CREATIVE_URL = HOSTNAME_URL + "/static/creative/" + CREATIVE_VERSION
    var DEFAULT_NATIVE_VIDEO_AD_URL = "$VERSIONED_CREATIVE_URL/vast/vast42-placement1.xml"
    var mRequestQueue: RequestQueue? = null
    var mStringRequest: StringRequest? = null
    var vastUrl: String? = null
    var vastUrl1: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        applyButton = findViewById(R.id.applyButton)
        urlEdt = findViewById(R.id.urlEdt)

        // When user click Apply button sendAndRequestResponse() Called
        applyButton.setOnClickListener(View.OnClickListener {
             vastUrl= urlEdt.text.toString()

            if (vastUrl!!.length == 0) {
                sendAndRequestResponse()
            }

            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

        })

    }

    // This method calle VASTParserAsyncTask() Method and execute the response which got during Parsing
    private fun sendAndRequestResponse() {
        //RequestQueue initialized

        mRequestQueue = Volley.newRequestQueue(this)

        //String Request initialized
        mStringRequest = StringRequest(
            Request.Method.GET, DEFAULT_NATIVE_VIDEO_AD_URL,
            { response ->
                val parser = VastParser()
                try {
                    val a = parser.parse(DEFAULT_NATIVE_VIDEO_AD_URL)
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
            val parser = VastParser()
            return try {
                params[0]?.let { parser.parse(it) }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(response: VASTResponse?) {
            // It checks a vasturl is null if null then it sets Edit text or user entered value
            if(vastUrl1 == null){
                var userUrl = urlEdt.text.toString()
                vastUrl1 = userUrl
            }
            //Redirect user to video player page
            val intent = Intent(this@HomeActivity, VASTActivity::class.java)
            startActivity(intent)

            vastUrl1 = null
        }
    }


}