package com.example.vast_ctv_android.VAST

abstract class VASTAdMediaDataParser : VASTBaseParser() {
    // have getAdMediaData abstract function which was override in VASTLinearParser class
    abstract fun getAdMediaData(): AdMediaData?
}