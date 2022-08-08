package com.example.vast_ctv_android.VAST

abstract class VASTAdMediaDataParser : VASTBaseParser() {
    abstract fun getAdMediaData(): AdMediaData?
}