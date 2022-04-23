package com.mondia.music.base.network.response

data class Authorization(val token: String, val type: String, val expiresIn: Long) {

    var json: String? = null

    override fun toString(): String = json ?: super.toString()
}