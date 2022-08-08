package com.integralads.omid.iassdk.videoad.vast

import com.example.vast_ctv_android.VAST.LinearAdMediaData
import java.util.*

class VASTResponse {
    val ads: ArrayList<String> = ArrayList()
    fun needLoadWrapper(): Boolean {
        return getAdData() != null
    }

    fun getWrapperVASTUrl(): String?{
        return LinearAdMediaData().videoUrl
    }

    fun getAdData(): String? {

        return if (LinearAdMediaData().videoUrl != null) LinearAdMediaData().videoUrl else null
    }
}