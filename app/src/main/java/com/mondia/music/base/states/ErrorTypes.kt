package com.mondia.music.base.states

import androidx.annotation.IntDef
import com.mondia.music.base.states.ErrorTypes.Companion.HTTP_EXCEPTION
import com.mondia.music.base.states.ErrorTypes.Companion.IO_EXCEPTION
import com.mondia.music.base.states.ErrorTypes.Companion.SOCKET_TIME_EXCEPTION
import com.mondia.music.base.states.ErrorTypes.Companion.UNKNOWN_ERROR

@IntDef(IO_EXCEPTION, HTTP_EXCEPTION, SOCKET_TIME_EXCEPTION, UNKNOWN_ERROR)
annotation class ErrorTypes {

    companion object {
        const val IO_EXCEPTION = 1
        const val HTTP_EXCEPTION = 2
        const val SOCKET_TIME_EXCEPTION = 3
        const val UNKNOWN_ERROR = 4
    }
}