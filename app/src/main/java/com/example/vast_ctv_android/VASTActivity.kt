package com.example.vast_ctv_android

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vast_ctv_android.videoAd.Defines.TAG
import com.example.vast_ctv_android.videoAd.NetworkHelper.Companion.getInstance
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.ClientProtocolException
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.CloseableHttpResponse
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient
import java.io.IOException
import java.io.InputStream
import java.net.*


/**
 * Created by Manav Shah on 21/07/2022.
 */

class VASTActivity : AppCompatActivity() {
    //    lateinit var textView: TextView
    private lateinit var exoPlayerView: PlayerView
    private lateinit var constraintRoot: ConstraintLayout
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private lateinit var urlType: URLType
    private lateinit var videoUrl: String
    var VAST_VERSION = 4.2
    var AD_VAST_VERSION_4_2 = ""
    val HOSTNAME_URL = "http://mobile-static.adsafeprotected.com"
    val CREATIVE_VERSION = "omid-v1/certification-v013"
    val VERSIONED_CREATIVE_URL = HOSTNAME_URL + "/static/creative/" + CREATIVE_VERSION
    var DEFAULT_NATIVE_VIDEO_AD_URL = "$VERSIONED_CREATIVE_URL/vast/vast42-placement1.xml"
    var connection: HttpURLConnection? = null
    var items = ArrayList<String>()
    var headlines: ArrayList<String> = ArrayList()
    private var context: Context? = null
    var mRequestQueue: RequestQueue? = null
    var mStringRequest: StringRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vastactivity)

        // get URL from a home activity using sharedPreference and store it in a videoUrl variable.
        val sharedPreference = getSharedPreferences("VAST_URL", Context.MODE_PRIVATE)
        videoUrl = sharedPreference.getString(
            "URL",
            "https://thumbs.dreamstime.com/videothumb_large4328/43288790.mp4"
        ).toString()
        getNativeVideoAdUrl(videoUrl, 0)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        findView()
        initPlayer()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        sendAndRequestResponse()

    }

    @Throws(URISyntaxException::class, ClientProtocolException::class, IOException::class)
    fun getUrlData(url: String?): InputStream? {
        val client = DefaultHttpClient()
        val method = HttpGet(URI(url))
        val res: CloseableHttpResponse? = client.execute(method)
        println("res?.getEntity()?.getContent()")
        println(res?.getEntity()?.getContent())
        return res?.getEntity()?.getContent()
    }

    // find exoplayerview from XML
    private fun findView() {
        constraintRoot = findViewById(R.id.constraintRoot)
        exoPlayerView = findViewById(R.id.idExoPlayerVIew)
    }
    fun getInputStream(url: URL): InputStream? {
        return try {
            url.openConnection().getInputStream()
        } catch (e: IOException) {
            null
        }
    }
    fun heads(): ArrayList<String> {
        return headlines
    }
    // Initialize player and create media source
    private fun initPlayer() {
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        simpleExoPlayer.addListener(playerListner)
        exoPlayerView.setPlayer(simpleExoPlayer)

        // Create media source
        createMediaSource()
        simpleExoPlayer.setMediaSource(mediaSource)
        simpleExoPlayer.prepare()
    }

    // Lifecycle
    // when activity started or resumed
    override fun onResume() {
        super.onResume()
        //make video player playable
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.play()
    }

    // when activity paused
    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
        //make video player not playable
        simpleExoPlayer.playWhenReady = false
    }

    // when activity stoped
    override fun onStop() {
        super.onStop()
        simpleExoPlayer.pause()
        //make video player not playable
        simpleExoPlayer.playWhenReady = false
    }

    // when activity destroyed
    override fun onDestroy() {
        super.onDestroy()
        //remove listener of an exoplayer
        simpleExoPlayer.removeListener(playerListner)
        simpleExoPlayer.stop()
        simpleExoPlayer.clearMediaItems()

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    // Listener for a player and for check the video format and according to video format it's set controls.
    private var playerListner = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()

            //If video is in HLS format
            if (urlType == URLType.HLS) {
                // HLS stram doesn't need seekbar
                exoPlayerView.useController = false
            }

            //If video is in MP4 format
            if (urlType == URLType.MP4) {
                // HLS stram doesn't need seekbar
                exoPlayerView.useController = true
            }
        }

        //When player have any error like source error or something
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(this@VASTActivity, "${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // create media resource here playable URL has been declared also set user agent as per the video format
    private fun createMediaSource() {

        urlType = URLType.MP4
        // assign url of video which was going to play
        var url = URLEncoder.encode(videoUrl);
        println("urlurl")
        println(url)
        urlType.url = videoUrl

        simpleExoPlayer.seekTo(0)

        //check URL and type and according to URL type it's set controllers
        when (urlType) {

            //if url type is MP4
            URLType.MP4 -> {
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this, applicationInfo.name)
                )
                mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse(urlType.url))
                )
            }

            //if URL type is HLS
            URLType.HLS -> {
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this, applicationInfo.name)
                )
                mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse(urlType.url))
                )
            }
        }

    }



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

                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }

            }) { error -> Log.i(TAG, "Error :$error") }
        mRequestQueue!!.add(mStringRequest)
        println("mRequestQueue")
        println(mRequestQueue)
    }

    private inner class VASTParserAsyncTask : AsyncTask<String?, Void?, String>() {
        override fun doInBackground(vararg params: String?): String {
            val parser = VastParser()
            try {
                params[0]?.let { parser.parse(it) }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
            return params.toString()
        }
    }

    fun getNativeVideoAdUrl(adUrl: String, index: Int): String {
        AD_VAST_VERSION_4_2 = getAdUrl(adUrl, index, DEFAULT_NATIVE_VIDEO_AD_URL)
        println(AD_VAST_VERSION_4_2)
        return AD_VAST_VERSION_4_2
    }

    private fun getAdUrl(adUrl: String, index: Int, defaultStaticURL: String): String {
        return getAdUrl(adUrl, defaultStaticURL)
    }

    fun getAdUrl(adUrl: String?, defaultAdUrl: String): String {
        println(adUrl)
        println(defaultAdUrl)

        return if (videoUrl.isEmpty()) adUrl!! else defaultAdUrl
    }

    fun setDefaultURLs(baseURL: String) {
        DEFAULT_NATIVE_VIDEO_AD_URL = "$baseURL/vast/vast42-placement1.xml"
    }
}

//check and define URL type
enum class URLType(var url: String) {
    MP4(""), HLS("")
}