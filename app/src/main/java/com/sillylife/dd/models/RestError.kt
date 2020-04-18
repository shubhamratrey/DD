package com.sillylife.dd.models

import com.google.gson.annotations.SerializedName

class RestError {
    @SerializedName("error_message")
    var errorMessage: String = ""
    @SerializedName("error_code")
    var errorCode: String = ""
}
