package com.mondia.music.features.home

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

class HomeRepo : BaseRepo() {

    suspend fun authorizeClient(): IResult<Authorization>? {
        return withContext(Dispatchers.IO) {
            getExecutor().invoke(
                requestMethod = POST,
                path = "v0/api/gateway/token/client",
                additionalHeaders = mapOf(
                    Pair(NetworkExecutor.GATEWAY_HEADER_KEY, BuildConfig.GATEWAYKEY),
                    Pair(
                        NetworkExecutor.CONTENT_TYPE_HEADER_KEY,
                        NetworkExecutor.FORM_URL_ENCODED_CONTENT_TYPE
                    )
                ),
                parser = AuthorizationParser()
            )
        }
    }

    suspend fun searchFor(
        authorizationToken: String,
        query: String,
        limit: Int = 20
    ): IResult<List<Song>> {
        return withContext(Dispatchers.IO) {
            getExecutor().invoke(
                requestMethod = GET,
                path = "v2/api/sayt/flat?query=${query}&limit=${limit}",
                additionalHeaders = mapOf(
                    Pair(
                        NetworkExecutor.AUTHORIZATION_HEADER_KEY, authorizationToken
                    ),
                    Pair(NetworkExecutor.GATEWAY_HEADER_KEY, BuildConfig.GATEWAYKEY)
                ), parser = SearchParser()
            )!!
        }
    }

}