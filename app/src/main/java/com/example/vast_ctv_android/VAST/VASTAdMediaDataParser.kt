package com.example.vast_ctv_android.VAST

/**
 * Created by Manav Shah on 06/08/22
 */

abstract class VASTAdMediaDataParser : VASTBaseParser() {
    // have getAdMediaData abstract function which was override in VASTLinearParser class
    abstract fun getAdMediaData(): AdMediaData?
}