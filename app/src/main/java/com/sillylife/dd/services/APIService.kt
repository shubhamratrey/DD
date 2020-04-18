package com.sillylife.dd.services

import android.content.Context
import com.sillylife.dd.BuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object APIService {

    const val BASE_URL = "https://zoopzam.herokuapp.com"
//    const val BASE_URL = "http://192.168.29.152:8000"

    fun build(): IAPIService {
        val retrofit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient())
                .baseUrl(BASE_URL).build()
        return retrofit.create(IAPIService::class.java)
    }

    fun build(context: Context, cacheDuration: Long): IAPIService {
        val retrofit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient(context, cacheDuration))
                .baseUrl(BASE_URL).build()
        return retrofit.create(IAPIService::class.java)
    }

    fun build(timeout: Long, bearer: Boolean): IAPIService {
        val retrofit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient(timeout, bearer))
                .baseUrl(BASE_URL).build()
        return retrofit.create(IAPIService::class.java)
    }

    private fun okHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.interceptors().add(interceptor(0))
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG)
            httpClient.interceptors().add(httpLoggingInterceptor())
        return httpClient.build()
    }

    private fun okHttpClient(timeout: Long, bearer: Boolean): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.interceptors().add(if (bearer) interceptor(0) else interceptorWithOutBearer())
        httpClient.readTimeout(timeout, TimeUnit.SECONDS)
        httpClient.connectTimeout(timeout, TimeUnit.SECONDS)
        httpClient.writeTimeout(timeout, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG)
            httpClient.interceptors().add(httpLoggingInterceptor())
        return httpClient.build()
    }

    private fun okHttpClient(context: Context, cacheDuration: Long): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        val cacheSize = (10 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)
        httpClient.cache(myCache)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.interceptors().add(interceptor(cacheDuration))
        if (BuildConfig.DEBUG)
            httpClient.interceptors().add(httpLoggingInterceptor())
        return httpClient.build()
    }

    fun interceptorWithOutBearer(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.addHeader("content-type", "application/json")
            val request = requestBuilder.build()
            val response = chain.proceed(request)
            if (response.code() == 401) {
                handleUnauthorisedResponse(request.url().url().path)
            }
            response
        }
    }

    private fun interceptor(cacheDuration: Long): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.addHeader("content-type", "application/json")
            val latch: CountDownLatch = CountDownLatch(1)
//            if(!CommonUtil.textIsEmpty(FirebaseAuthUserManager.getFirebaseAuthToken())){
//                requestBuilder.addHeader("Authorization", "Bearer ${FirebaseAuthUserManager.getFirebaseAuthToken()}")
//            }
            FirebaseAuthUserManager.retrieveIdToken(false, object : FirebaseAuthUserManager.TokenRetrieveListener {
                override fun onTokenRetrieved(success: Boolean, token: String) {
                    if (success) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }
                    latch.countDown()
                }
            })
            try {
                latch.await()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            if (cacheDuration > 0) {
                requestBuilder.addHeader("Cache-Control", "public, max-age=$cacheDuration")
            }
            val request = requestBuilder.build()
            val response = chain.proceed(request)
            if (response.code() == 401) {
                handleUnauthorisedResponse(request.url().url().path)
            }
            response
        }
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    private fun handleUnauthorisedResponse(path: String) {
        FirebaseAuthUserManager.retrieveIdToken(true)
    }

}