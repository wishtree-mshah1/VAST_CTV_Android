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
import com.example.vast_ctv_android.VAST.*
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
            val intent = Intent(this@HomeActivity, VASTActivity::class.java)
            intent.putExtra("URL",DEFAULT_NATIVE_VIDEO_AD_URL)
            startActivity(intent)
            if (vastUrl!!.length == 0) {

//                sendAndRequestResponse()
            }

            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

        })

    }


}