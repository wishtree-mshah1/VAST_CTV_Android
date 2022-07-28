package com.example.vast_ctv_android.videoAd

object Defines {
    const val TAG = "Defines"
    const val HOSTNAME_URL = "http://mobile-static.adsafeprotected.com"
    const val CREATIVE_VERSION = "omid-v1/certification-v013"
    const val VERSIONED_CREATIVE_URL = HOSTNAME_URL + "/static/creative/" + CREATIVE_VERSION

    var DEFAULT_AD_URL: String? = null
    var DEFAULT_NATIVE_VIDEO_AD_URL: String? = null
    const val VIDEO_CLICK_URL = "http://integralads.com"
    const val USER_AGENT = "ias-demo-app-none-sdk"

    fun setDefaultURLs(baseURL: String) {
        DEFAULT_AD_URL = "$baseURL/html-display/index1.html"
        DEFAULT_NATIVE_VIDEO_AD_URL = "$baseURL/vast/vast41-placement1.xml"
    }
}