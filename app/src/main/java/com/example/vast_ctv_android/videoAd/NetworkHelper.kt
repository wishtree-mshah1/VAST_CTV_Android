package com.example.vast_ctv_android.videoAd

import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

class NetworkHelper private constructor(context: Context?) {
    private val mRequestQueue: RequestQueue
    private var mImageLoader: ImageLoader? = null
    fun addToRequestQueue(req: Request<*>?) {
        mRequestQueue.add(req)
    }

    fun getImageLoader(): ImageLoader {
            if (mImageLoader == null) {
                mImageLoader = ImageLoader(mRequestQueue, MyImageCache())
            }
            return mImageLoader!!
        }

    private class MyImageCache : ImageLoader.ImageCache {
        private val cache = LruCache<String, Bitmap>(20)
        override fun getBitmap(url: String): Bitmap? {
            return cache[url]
        }

        override fun putBitmap(url: String, bitmap: Bitmap) {
            cache.put(url, bitmap)
        }
    }

    companion object {
        @JvmStatic
        private var mInstance: NetworkHelper? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context?): NetworkHelper {
            if (mInstance == null) {
                mInstance = NetworkHelper(context)
            }
            return mInstance!!
        }
    }

    init {
        // getApplicationContext() is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        val mCtx = context?.applicationContext
        mRequestQueue = Volley.newRequestQueue(mCtx)
    }
}