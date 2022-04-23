package com.mondia.music.base.network.parser

import android.util.Log
import com.mondia.music.base.network.JsonParser
import com.mondia.music.base.network.response.Artist
import com.mondia.music.base.network.response.Cover
import com.mondia.music.base.network.response.Song
import org.json.JSONArray

class SearchParser : JsonParser<List<Song>> {

    override fun toJson(model: List<Song>): String? {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: String): List<Song>? {
        val songsList: List<Song>? = try {
            val mappedSongsList: MutableList<Song> = mutableListOf()

            val jsonArray = JSONArray(json)
            for (index in 0 until jsonArray.length()) {
                val songJsonObject = jsonArray.getJSONObject(index)
                mappedSongsList.add(
                    Song(
                        id = if (songJsonObject.has("id")) songJsonObject.getLong("id") else 0,
                        type = if (songJsonObject.has("type")) songJsonObject.getString("type") else null,
                        title = if (songJsonObject.has("title")) songJsonObject.getString("title") else null,
                        duration = if (songJsonObject.has("duration")) songJsonObject.getLong("duration") else 0,
                        publishDate = if (songJsonObject.has("publishingDate")) songJsonObject.getString(
                            "publishingDate"
                        ) else null,
                        artist = if (songJsonObject.has("mainArtist")) Artist(
                            id = if (songJsonObject.getJSONObject("mainArtist")
                                    .has("id")
                            ) songJsonObject.getJSONObject("mainArtist").getLong("id") else 0,
                            name = if (songJsonObject.getJSONObject("mainArtist")
                                    .has("name")
                            ) songJsonObject.getJSONObject("mainArtist").getString("name") else null
                        ) else null,
                        volumeNumber = if (songJsonObject.has("volumeNumber")) songJsonObject.getLong(
                            "volumeNumber"
                        ) else 0,
                        trackNumber = if (songJsonObject.has("trackNumber")) songJsonObject.getLong(
                            "trackNumber"
                        ) else 0,
                        cover = if (songJsonObject.has("cover")) Cover(
                            tiny = if (songJsonObject.getJSONObject("cover")
                                    .has("tiny")
                            ) songJsonObject.getJSONObject("cover").getString("tiny") else null,
                            small = if (songJsonObject.getJSONObject("cover")
                                    .has("small")
                            ) songJsonObject.getJSONObject("cover").getString("small") else null,
                            medium = if (songJsonObject.getJSONObject("cover")
                                    .has("medium")
                            ) songJsonObject.getJSONObject("cover").getString("medium") else null,
                            large = if (songJsonObject.getJSONObject("cover")
                                    .has("large")
                            ) songJsonObject.getJSONObject("cover").getString("large") else null
                        ) else null
                    )
                )
            }
            mappedSongsList
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "fromJson: ${e.localizedMessage}", e)
            null
        }
        return songsList
    }
}