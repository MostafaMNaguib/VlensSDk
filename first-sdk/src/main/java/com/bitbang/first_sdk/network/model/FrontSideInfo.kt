package com.bitbang.first_sdk.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FrontSideInfo(
    val address: String,
    val dateOfBirth: String,
    val govern: String,
    val idKey: String,
    val idNumber: String,
    val name: String
): Parcelable