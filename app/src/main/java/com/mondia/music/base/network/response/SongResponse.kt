package com.mondia.music.base.network.response

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
    val id: Long,
    val type: String?,
    val title: String?,
    val duration: Long,
    val publishDate: String?,
    val artist: Artist?,
    val volumeNumber: Long,
    val trackNumber: Long,
    val cover: Cover?
) : Parcelable

@Parcelize
data class Artist(val id: Long, val name: String?) : Parcelable

@Parcelize
data class Cover(val tiny: String?, val small: String?, val medium: String?, val large: String?) :
    Parcelable {
    @IgnoredOnParcel
    var tinyBitmap: Bitmap? = null

    @IgnoredOnParcel
    var smallBitmap: Bitmap? = null

    @IgnoredOnParcel
    var mediumBitmap: Bitmap? = null

    @IgnoredOnParcel
    var largeBitmap: Bitmap? = null
}