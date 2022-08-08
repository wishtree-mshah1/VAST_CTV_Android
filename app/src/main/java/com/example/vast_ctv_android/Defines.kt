package com.example.vast_ctv_android

object Defines {
    const val TAG = "Defines"
    val HOSTNAME_URL = "http://mobile-static.adsafeprotected.com"
    val CREATIVE_VERSION = "omid-v1/certification-v013"
    val VERSIONED_CREATIVE_URL = HOSTNAME_URL + "/static/creative/" + CREATIVE_VERSION
    var DEFAULT_NATIVE_VIDEO_AD_URL = "$VERSIONED_CREATIVE_URL/vast/vast42-placement1.xml"

}