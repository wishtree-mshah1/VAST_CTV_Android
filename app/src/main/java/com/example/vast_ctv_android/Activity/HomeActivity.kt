package com.example.vast_ctv_android.Activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import android.widget.Toast
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
import java.io.IOException
import java.net.HttpURLConnection
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Manav Shah on 20/07/2022.
 * Email :- manav.shah@wishtreetech.com
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


    fun isValidURL(url: String?): Boolean {
        // Regex to check valid URL
        val regex = ("((http|https)://)(www.)?"
                + "[a-zA-Z0-9@:%._\\+~#?&//=]"
                + "{2,256}\\.[a-z]"
                + "{2,6}\\b([-a-zA-Z0-9@:%"
                + "._\\+~#?&//=]*)")

        // Compile the ReGex
        val p: Pattern = Pattern.compile(regex)

        // If the string is empty
        // return false
        if (url == null) {
            return false
        }

        // Find match between given string
        // and regular expression
        // using Pattern.matcher()
        val m: Matcher = p.matcher(url)

        // Return if the string
        // matched the ReGex
        return m.matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        applyButton = findViewById(R.id.applyButton)
        loadButton = findViewById(R.id.loadButton)
        urlEdt = findViewById(R.id.urlEdt)

        //Onclick of load button sendAndRequestResponse() Called
        loadButton.setOnClickListener(View.OnClickListener {
             vastUrl= urlEdt.text.toString()
            println("vastUrl")
            println(vastUrl)
            if (vastUrl!!.length == 0) {
                sendAndRequestResponse()
            }

            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

        })

        // When user click Apply button
        applyButton.setOnClickListener(View.OnClickListener {

            // It checks a vasturl is null if null then it sets Edit text or user entered value
            if(vastUrl1 == null){
                var userUrl = urlEdt.text.toString()
                vastUrl1 = userUrl
            }
            //Set VAST URL for video player page
            val sharedPreference =  getSharedPreferences("VAST_URL",Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("URL", vastUrl1.toString())
            editor.commit()

            //Redirect user to video player page
            val intent = Intent(this@HomeActivity, VASTActivity::class.java)
            startActivity(intent)

            vastUrl1 = null

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
                    println("responseee")
                    val a = parser.parse(DEFAULT_NATIVE_VIDEO_AD_URL)
                    val b = VASTParserAsyncTask()
                    println("yeeeeee")
                    b.execute(response)
                    println(b)
                    println(a)

                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }

            }) { error -> Log.i("Error", "Error :$error") }

        mRequestQueue!!.add(mStringRequest)
        println("mRequestQueue")
        println(mRequestQueue)
    }

    // This block of code perform Async Task it parse the url to parser and get the video URL after parsing and set it as a Video URL
    private inner class VASTParserAsyncTask : AsyncTask<String?, Void?, String>() {
        override fun doInBackground(vararg params: String?): String {
            var a :String = ""
            val parser = VastParser()
            try {
                params[0]?.let { vastUrl1 = parser.parse(it).toString()
                println(vastUrl1)}
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
            return a
        }
    }


}