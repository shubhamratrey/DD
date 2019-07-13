package com.sillylife.dd.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MixedDataItem(
        var title: String?,
        var uri: String?,
        var type: String?,
        var subType: String?,
        var status: Boolean?,
        var date: String?,
        var time: String?)
    : Parcelable {
    constructor(title: String?, type: String?) : this(null, null, null, null, false, null, null) {
        this.title = title
        this.type = type
    }
}
