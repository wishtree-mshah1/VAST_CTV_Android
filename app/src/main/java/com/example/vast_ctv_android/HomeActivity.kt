package com.example.vast_ctv_android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import android.content.SharedPreferences
import android.preference.PreferenceManager

class HomeActivity : AppCompatActivity() {
    lateinit var  applyButton: Button
    lateinit var  urlEdt: TextInputEditText
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        applyButton = findViewById(R.id.applyButton)
        urlEdt = findViewById(R.id.urlEdt)
        applyButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@HomeActivity, VASTActivity::class.java)
            var vastUrl:String = urlEdt.text.toString()
            println("vastUrl")
            println(vastUrl)
            if (vastUrl!!.length == 0) {
                vastUrl = "https://thumbs.dreamstime.com/videothumb_large4328/43288790.mp4"
            }
            intent.putExtra("abc", vastUrl)
            startActivity(intent)
            val sharedPreference =  getSharedPreferences("VAST_URL",Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("URL", vastUrl.toString())
            editor.commit()

        })
    }
}