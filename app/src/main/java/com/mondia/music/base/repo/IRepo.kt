package com.mondia.music.base.repo

import com.mondia.music.base.MusicApp
import com.mondia.music.base.network.NetworkExecutor

interface IRepo {
    fun getExecutor(): NetworkExecutor = MusicApp.executor
}