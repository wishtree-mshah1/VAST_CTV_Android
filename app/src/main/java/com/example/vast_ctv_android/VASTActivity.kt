package com.example.vast_ctv_android

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class VASTActivity : AppCompatActivity() {
//    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vastactivity)

        val sharedPreference =  getSharedPreferences("VAST_URL", Context.MODE_PRIVATE)
        val videoUrl = sharedPreference.getString("URL","https://thumbs.dreamstime.com/videothumb_large4328/43288790.mp4")
//        textView = findViewById(R.id.txtview)
//        textView.setText(videoUrl)


    }
}