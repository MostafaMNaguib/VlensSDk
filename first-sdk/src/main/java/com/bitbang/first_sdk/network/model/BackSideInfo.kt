package com.bitbang.first_sdk.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BackSideInfo(
    val gender: String,
    val husbandName: String,
    val idExpiry: String,
    val job: String,
    val maritalStatus: String,
    val religion: String
): Parcelable