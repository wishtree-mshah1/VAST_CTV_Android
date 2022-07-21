package com.example.vast_ctv_android

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.net.URLEncoder

class VASTActivity : AppCompatActivity() {
//    lateinit var textView: TextView
    private lateinit var exoPlayerView: PlayerControlView
    private lateinit var constraintRoot: ConstraintLayout
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private lateinit var urlType: URLType
    private lateinit var videoUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vastactivity)
//        if (Build.VERSION.SDK_INT >= 21) {
//            mWebView.getSettings().setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
//        }
        val sharedPreference =  getSharedPreferences("VAST_URL", Context.MODE_PRIVATE)
        videoUrl = sharedPreference.getString("URL","https://thumbs.dreamstime.com/videothumb_large4328/43288790.mp4")
            .toString()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        findView()
        initPlayer()
    }

    // find exoplayerview from XML
    private fun findView(){
        constraintRoot = findViewById(R.id.constraintRoot)
        exoPlayerView = findViewById(R.id.idExoPlayerVIew)
    }

    // Initialize player and create media source
    private fun initPlayer(){
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        simpleExoPlayer.addListener(playerListner)
        // Create media source
        createMediaSource()
        simpleExoPlayer.setMediaSource(mediaSource)
        simpleExoPlayer.prepare()
    }

    // Lifecycle
    // when activity started or resumed
    override fun onResume() {
        super.onResume()
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.play()
    }
    // when activity paused
    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
        simpleExoPlayer.playWhenReady = false
    }
    // when activity stoped
    override fun onStop() {
        super.onStop()
        simpleExoPlayer.pause()
        simpleExoPlayer.playWhenReady = false
    }
    // when activity destroyed
    override fun onDestroy() {
        super.onDestroy()

        simpleExoPlayer.removeListener(playerListner)
        simpleExoPlayer.stop()
        simpleExoPlayer.clearMediaItems()

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    // Listener for a player and for check the video format and according to video format it's set controls.
    private var playerListner = object : Player.Listener{
        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()

//            //If video is in HLS format
//            if(urlType == URLType.HLS){
//                // HLS stram doesn't need seekbar
//                exoPlayerView.useController = false
//            }
//
//            //If video is in MP4 format
//            if(urlType == URLType.MP4){
//                // HLS stram doesn't need seekbar
//                exoPlayerView.useController = true
//            }
        }

        //When player have any error like source error or something
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(this@VASTActivity,"${error.message}",Toast.LENGTH_SHORT).show()
        }
    }

    // create media resource here playable URL has been declared also set user agent as per the video format
    private fun createMediaSource(){
        urlType = URLType.MP4
        // assign url of video which was going to play
        var url = URLEncoder.encode("https://jsoncompare.org/LearningContainer/SampleFiles/Video/MP4/Sample-MP4-Video-File-for-Testing.mp4");
        println("urlurl")
        println(url)
        urlType.url = "https://jsoncompare.org/LearningContainer/SampleFiles/Video/MP4/Sample-MP4-Video-File-for-Testing.mp4"

        simpleExoPlayer.seekTo(0)
        when(urlType){

            //if url type is MP4
            URLType.MP4 ->{

                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this,applicationInfo.name)
                )
                mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse("https://jsoncompare.org/LearningContainer/SampleFiles/Video/MP4/Sample-MP4-Video-File-for-Testing.mp4"))
                )
            }
            //if URL type is HLS
            URLType.HLS->{
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this,applicationInfo.name)
                )
                mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse(urlType.url))
                )
            }
        }

    }


}
//check and define URL type
enum class URLType(var url: String){
    MP4(""), HLS("")
}