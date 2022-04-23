package com.mondia.music.base.network

import android.util.Log
import androidx.annotation.IntDef
import com.mondia.music.BuildConfig
import com.mondia.music.base.network.NetworkExecutor.RequestMethod.Companion.GET
import com.mondia.music.base.network.NetworkExecutor.RequestMethod.Companion.POST
import com.mondia.music.base.states.ErrorTypes.Companion.HTTP_EXCEPTION
import com.mondia.music.base.states.ErrorTypes.Companion.IO_EXCEPTION
import com.mondia.music.base.states.ErrorTypes.Companion.SOCKET_TIME_EXCEPTION
import com.mondia.music.base.states.ErrorTypes.Companion.UNKNOWN_ERROR
import com.mondia.music.base.states.IResult
import com.mondia.music.base.states.ResponseResult
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class NetworkExecutor {

    inline operator fun <reified T> invoke(
        @RequestMethod requestMethod: Int,
        baseUrl: String = BuildConfig.BASE_URL,
        path: String,
        body: String? = null,
        additionalHeaders: Map<String, String> = emptyMap(),
        parser: JsonParser<T>? = null
    ): IResult<T>? {
        val url = URL("${baseUrl}${path}")
        (url.openConnection() as? HttpURLConnection)?.run {
            this.requestMethod = when (requestMethod) {
                POST -> "POST"
                GET -> "GET"
                else -> null
            }
            setRequestProperty(
                CONTENT_TYPE_HEADER_KEY,
                additionalHeaders[CONTENT_TYPE_HEADER_KEY] ?: "application/json; utf-8"
            )
            setRequestProperty(
                ACCEPT_HEADER_KEY,
                additionalHeaders[ACCEPT_HEADER_KEY] ?: "application/json"
            )
            additionalHeaders.forEach {
                if (it.key.isNotEmpty() && it.value.isNotEmpty())
                    setRequestProperty(it.key, it.value)
            }
            body?.let {
                doOutput = true
                outputStream.buffered().write(it.toByteArray())
            }
            try {
                val response: StringBuilder = StringBuilder()
                inputStream.buffered().reader().readLines().forEach {
                    response.append(it)
                }
                Log.d(TAG, "response: $responseCode -> $response")
                return ResponseResult.success(parser?.fromJson(response.toString()))
            } catch (e: Exception) {
                Log.e(
                    NetworkExecutor::class.java.simpleName,
                    "error: ${e.localizedMessage}",
                    e.cause
                )
                when (e) {
                    is IOException -> return ResponseResult.error(e.localizedMessage, IO_EXCEPTION)
                    is SocketTimeoutException -> return ResponseResult.error(
                        e.localizedMessage,
                        SOCKET_TIME_EXCEPTION
                    )
                    else -> {
                        if (errorStream != null) {
                            val error: StringBuilder = StringBuilder()
                            errorStream.buffered().reader().readLines().forEach {
                                error.append(it)
                            }
                            return ResponseResult.error(error.toString(), HTTP_EXCEPTION)
                        } else {
                            return ResponseResult.error(null, UNKNOWN_ERROR)
                        }
                    }
                }
            } finally {
                this.disconnect()
            }
        }
        return null
    }

    @IntDef(POST, GET)
    annotation class RequestMethod {
        companion object {
            const val POST = 1
            const val GET = 2
        }
    }

    companion object {
        const val GATEWAY_HEADER_KEY = "X-MM-GATEWAY-KEY"
        const val AUTHORIZATION_HEADER_KEY = "Authorization"
        const val CONTENT_TYPE_HEADER_KEY = "Content-Type"
        const val ACCEPT_HEADER_KEY = "Accept"
        const val FORM_URL_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded"
        const val APPLICATION_JSON_UTF_8_CONTENT_TYPE = "application/json; utf-8"
        val TAG = "SAADSAAD"
    }
}