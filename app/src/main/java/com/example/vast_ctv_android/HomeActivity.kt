package com.example.vast_ctv_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Manav Shah on 20/07/2022.
 */

class HomeActivity : AppCompatActivity() {
    lateinit var  applyButton: Button
    lateinit var  urlEdt: TextInputEditText
    private lateinit var sharedPreferences: SharedPreferences

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
        urlEdt = findViewById(R.id.urlEdt)
        applyButton.setOnClickListener(View.OnClickListener {
            var vastUrl:String = urlEdt.text.toString()
            println("vastUrl")
            println(vastUrl)
            if (vastUrl!!.length == 0) {
                vastUrl = "https://jsoncompare.org/LearningContainer/SampleFiles/Video/MP4/Sample-MP4-Video-File-for-Testing.mp4"
            }
            if (isValidURL(vastUrl) == true) {
                val intent = Intent(this@HomeActivity, VASTActivity::class.java)
                startActivity(intent)
            }
            else{
                val toast = Toast.makeText(applicationContext, "Wrong URL", Toast.LENGTH_SHORT)
                toast.show()
            }



            val sharedPreference =  getSharedPreferences("VAST_URL",Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString("URL", vastUrl.toString())
            editor.commit()

        })
    }
}