package com.sillylife.dd.services

import com.sillylife.dd.constants.NetworkConstants
import com.sillylife.dd.models.HomeDataResponse
import com.sillylife.zoopzam.models.responses.*
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface IAPIService {

    @GET("${NetworkConstants.V1}/home/all/")
    fun getHomeData(@QueryMap queryMap: Map<String, String>): Observable<Response<HomeDataResponse>>

    @FormUrlEncoded
    @POST("${NetworkConstants.V1}/users/register-fcm/")
    fun registerFCM(
            @Field("app_name") appName: String,
            @Field("os_type") osType: String,
            @Field("app_instance_id") appInstanceId: String,
            @Field("app_build_number") appBuildNumber: Int,
            @Field("installed_version") installedVersion: String,
            @Field("fcm_token") fcmToken: String
    ): Observable<Response<GenericResponse>>

    @FormUrlEncoded
    @POST("${NetworkConstants.V1}/users/unregister-fcm/")
    fun unregisterFCM(@Field("fcm_token") fcmToken: String): Observable<Response<String>>

}