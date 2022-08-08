package com.example.vast_ctv_android.VAST

import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.vast_ctv_android.Defines
import com.example.vast_ctv_android.R
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
import com.google.android.exoplayer2.util.Util
import com.integralads.omid.iassdk.videoad.base.BaseVideoAd
import com.integralads.omid.iassdk.videoad.vast.VASTResponse
import java.net.URLEncoder
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Created by Manav Shah on 21/07/2022.
 */


open class VASTActivity : AppCompatActivity() {

    interface VideoAdListener {
        fun onVideoAdFailed(videoAd: VASTActivity)
        fun onVideoAdLoaded(videoAd: String)
        fun getContentURL(): String?
    }
    private lateinit var exoPlayerView: PlayerView
    private lateinit var constraintRoot: ConstraintLayout
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private lateinit var urlType: URLType
    private var videoUrl: String? = null
    private var VASTURL: String? = null

    fun isValidURL(url: String?): Boolean {
        // Regex to check valid URL
        val regex = "(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" +
                "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" +
                "|mil|biz|info|mobi|name|aero|jobs|museum" +
                "|travel|[a-z]{2}))(:[\\d]{1,5})?" +
                "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" +
                "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" +
                "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" +
                "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b"+"mp4"

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
        setContentView(R.layout.activity_vastactivity)

        val extras = intent.extras
        VASTURL = extras?.getString("URL")

        loadAd(VASTURL.toString(),this)

        if (videoUrl != null) {
            if (isValidURL(videoUrl) ==  false){
                Toast.makeText(this@VASTActivity, "URL is not in proper format", Toast.LENGTH_SHORT).show()
            }
            else{

            }
        }
        else{
            Toast.makeText(this@VASTActivity, "Don't have playable URL", Toast.LENGTH_SHORT).show()
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
//        sendAndRequestResponse()

    }

    @JvmField
    var listener: VASTActivity.VideoAdListener? = null
    protected var videoAdLoader: VideoLoader? = null


    fun loadAd(url: String?, context: VASTActivity) {
        val adUrl = Defines.DEFAULT_NATIVE_VIDEO_AD_URL
        videoAdLoader = VideoLoader()
        videoAdLoader?.loadUrl(adUrl.toString(), context , object : VideoLoader.VideoAdLoaderListener {
            override fun loadedVASTResponse(reponse: VASTResponse?) {
                videoUrl = reponse?.ads?.get(0).toString()
                listener?.onVideoAdLoaded(reponse?.ads?.get(0).toString())
                findView()
                initPlayer()
            }

            override fun failedLoadVASTResponse() {
                adLoadFailed()
            }
        })
    }
    private fun adLoadFailed() {
        println("Failed to load ad")
    }
    // find exoplayerview from XML
    private fun findView() {
        constraintRoot = findViewById(R.id.constraintRoot)
        exoPlayerView = findViewById(R.id.idExoPlayerVIew)
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
//    override fun onResume() {
//        super.onResume()
//        //make video player playable
//        simpleExoPlayer.playWhenReady = true
//        simpleExoPlayer.play()
//    }
//
//    // when activity paused
//    override fun onPause() {
//        super.onPause()
//        simpleExoPlayer.pause()
//        //make video player not playable
//        simpleExoPlayer.playWhenReady = false
//    }
//
//    // when activity stoped
//    override fun onStop() {
//        super.onStop()
//        simpleExoPlayer.pause()
//        //make video player not playable
//        simpleExoPlayer.playWhenReady = false
//    }
//
//    // when activity destroyed
//    override fun onDestroy() {
//        super.onDestroy()
//        //remove listener of an exoplayer
//        simpleExoPlayer.removeListener(playerListner)
//        simpleExoPlayer.stop()
//        simpleExoPlayer.clearMediaItems()
//
//        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//    }

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

        urlType.url = videoUrl.toString()

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

}

//check and define URL type
enum class URLType(var url: String) {
    MP4(""), HLS("")
}