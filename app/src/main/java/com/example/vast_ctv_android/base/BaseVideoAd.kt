package com.integralads.omid.iassdk.videoad.base

import android.content.Context
import android.text.TextUtils
import android.webkit.WebView
import com.example.vast_ctv_android.Defines
import com.example.vast_ctv_android.VAST.VASTActivity
import com.example.vast_ctv_android.VAST.VideoLoader
import com.integralads.omid.iassdk.videoad.vast.VASTResponse
import java.util.*

class BaseVideoAd() {
    interface VideoAdListener {
        fun onVideoAdFailed(videoAd: BaseVideoAd)
        fun onVideoAdLoaded(videoAd: String)
        fun getContentURL(): String?
    }

    @JvmField
    var listener: VideoAdListener? = null
    protected var videoAdLoader: VideoLoader? = null


    fun loadAd(url: String?, context: VASTActivity) {
        val adUrl = Defines.DEFAULT_NATIVE_VIDEO_AD_URL
        videoAdLoader = VideoLoader()
        videoAdLoader?.loadUrl(adUrl.toString(), context , object : VideoLoader.VideoAdLoaderListener {
            override fun loadedVASTResponse(reponse: VASTResponse?) {
                listener?.onVideoAdLoaded(reponse?.ads?.get(0).toString())
            }

            override fun failedLoadVASTResponse() {
                adLoadFailed()
            }
        })
    }

    private fun adLoadFailed() {
        println("Failed to load ad")
    }

//    abstract fun showAd()
//    abstract fun preLoadMedia(): Boolean



}