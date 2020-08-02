package br.com.iddog.data.model.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val email: String,
    val token: String
) : Parcelable