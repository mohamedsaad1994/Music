package com.mondia.music.base.repo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.mondia.music.BuildConfig
import com.mondia.music.base.network.NetworkExecutor
import com.mondia.music.base.network.NetworkExecutor.RequestMethod.Companion.GET
import com.mondia.music.base.network.NetworkExecutor.RequestMethod.Companion.POST
import com.mondia.music.base.network.parser.AuthorizationParser
import com.mondia.music.base.network.parser.SearchParser
import com.mondia.music.base.network.response.Authorization
import com.mondia.music.base.network.response.Song
import com.mondia.music.base.repo.BaseRepo
import com.mondia.music.base.states.IResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class ImageRepo : BaseRepo() {
    suspend fun getBitmap(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val url = URL(if (url.contains("http")) url else "http:$url")
            val bitmap = try {
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: Exception) {
                null
            }
            return@withContext bitmap
        }
    }
}