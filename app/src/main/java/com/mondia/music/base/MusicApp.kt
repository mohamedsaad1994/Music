package com.mondia.music.base

import android.app.Application
import com.mondia.music.base.network.NetworkExecutor

class MusicApp : Application() {

    companion object {
        val executor = NetworkExecutor()
    }

}