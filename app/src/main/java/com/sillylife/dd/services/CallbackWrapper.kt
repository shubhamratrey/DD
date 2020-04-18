package com.sillylife.dd.services

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.sillylife.dd.models.RestError
import com.sillylife.dd.models.responses.BasicResponse
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class CallbackWrapper<T> : DisposableObserver<T> {

    var episodeEntityId: Int = -1
    protected abstract fun onSuccess(t: T)

    protected abstract fun onFailure(code: Int, message: String)

    constructor() {}

    override fun onComplete() {

    }

    override fun onError(e: Throwable) {
        //404 Not Found
        //        500 Internal Server Error
//        hideShimmer()

        if (e is HttpException) {
            e.printStackTrace()
            val responseBody = e.response().errorBody()
            val error = getErrorMessage(responseBody)
            logFailure(e.code(), error.toString())
        } else if (e is SocketTimeoutException || e is UnknownHostException) {
            e.printStackTrace()
            var httpStatus: HTTPStatus = HTTPStatus.SOCKET_TIMEOUT
            logFailure(httpStatus.code, httpStatus.message)
        } else if (e is IOException) {
            e.printStackTrace()
            var httpStatus: HTTPStatus = HTTPStatus.FORBIDDEN
            logFailure(httpStatus.code, httpStatus.message)
        } else if (e is JsonSyntaxException) {
            e.printStackTrace()
            var httpStatus: HTTPStatus = HTTPStatus.SERVER_ERROR
            logFailure(httpStatus.code, httpStatus.message)
        } else {
            logFailure(HTTPStatus.BAD_REQUEST.code, e.message.toString());
        }
    }

    override fun onNext(t: T) {
//        hideShimmer()
        val br = t as Response<BasicResponse<*>>
        if (br.code() in 200..299) {
            onSuccess(t)
        } else {
            val code = br.code()
            if (code == HTTPStatus.NOT_FOUND.code) {
                logFailure(HTTPStatus.NOT_FOUND.code, HTTPStatus.NOT_FOUND.message)
            } else if (code == HTTPStatus.SERVER_ERROR.code) {
                logFailure(HTTPStatus.SERVER_ERROR.code, HTTPStatus.SERVER_ERROR.message)
            } else {
                val restError = handleError(br.errorBody())
                logFailure(HTTPStatus.BAD_REQUEST.code, restError.errorMessage)
            }
        }
    }

    fun logFailure(httpStatus: HTTPStatus) {
        Log.d("Failure", httpStatus.message)
        onFailure(httpStatus.code, httpStatus.message)
    }

    fun logFailure(code: Int, message: String) {
        Log.d("Failure", message)
        onFailure(code, message)
    }

    fun getErrorMessage(responseBody: ResponseBody?): String? {
        try {
            val jsonObject = JSONObject(responseBody!!.string())
            return jsonObject.getString("message")
        } catch (e: Exception) {
            return e.message
        }

    }

    fun handleError(response: ResponseBody?): RestError {
        var restError = RestError()
        restError.errorMessage = "Something Went Wrong!"
        if (response is ResponseBody) {
            try {
                restError = Gson().fromJson(String(response.bytes()), RestError::class.java)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                restError.errorMessage = "JsonSyntaxException Occurred"
            } catch (e: UndeliverableException) {
                e.printStackTrace()
                restError.errorMessage = "UndeliverableException Occurred"
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                restError.errorMessage = "IllegalStateException Occurred"
            }
        }
        return restError
    }

}