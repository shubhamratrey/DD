package com.sillylife.dd.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.android.parcel.Parcelize

@Parcelize
data class  HomeDataResponse(@SerializedName("items") var dataItems: ArrayList<String>?) : Parcelable