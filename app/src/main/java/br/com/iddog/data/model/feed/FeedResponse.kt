package br.com.iddog.data.model.feed

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedResponse(
    val category: String,
    val list: List<String>
) : Parcelable