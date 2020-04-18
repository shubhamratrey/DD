package com.sillylife.dd.services

import android.annotation.SuppressLint
import android.os.Handler
import android.os.HandlerThread
import android.text.TextUtils
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.sillylife.dd.BuildConfig
import com.sillylife.dd.DDApplication
import com.sillylife.dd.services.sharedpreference.SharedPreferenceManager
import com.sillylife.zoopzam.models.responses.GenericResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object FirebaseAuthUserManager {

    private val TAG = FirebaseAuthUserManager::class.java.name
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var mIdTokenListener: FirebaseAuth.IdTokenListener? = null
    private var firebaseUser: FirebaseUser? = null
    private var appDisposable: AppDisposable? = null

    private var firebaseTokenRefreshHandlerThread: HandlerThread? = null
    private var firebaseTokenRefreshHandler: Handler? = null
    private val firebaseTokenRefreshRunnable = Runnable {
        Log.d(TAG, "inside firebaseTokenRefreshRunnable")
        retrieveIdToken(true)
    }

    init {
        try {
            FirebaseApp.getInstance()
        } catch (e: Exception) {
            FirebaseApp.initializeApp(DDApplication.getInstance())
        }
        firebaseTokenRefreshHandlerThread = HandlerThread("AuthTokenRefreshHandlerThread")
        firebaseTokenRefreshHandlerThread!!.start()
        firebaseTokenRefreshHandler = Handler(firebaseTokenRefreshHandlerThread!!.looper)
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                firebaseUser!!.getIdToken(false).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        SharedPreferenceManager.storeFirebaseAuthToken(task.result!!.token!!)
                        registerFCMToken()
                    }
                }
            } else {
                Log.d(TAG, "Firebase user is null")
                unregisterFCMToken()
                SharedPreferenceManager.storeFirebaseAuthToken("")
            }
        }
        firebaseUser = FirebaseAuth.getInstance().currentUser
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener!!)
        startListeningForIdTokenChanges()
        retrieveIdToken(true)
    }

    fun retrieveIdToken(refresh: Boolean) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(refresh)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val timeDiff = task.result!!.expirationTimestamp * 1000 - System.currentTimeMillis()
                if (timeDiff <= 0) {
                    retrieveIdToken(true)
                } else {
                    firebaseTokenRefreshHandler?.postDelayed(firebaseTokenRefreshRunnable, timeDiff)
                }
                SharedPreferenceManager.storeFirebaseAuthToken(task.result!!.token!!)
            }
        }
    }

    fun retrieveIdToken(refresh: Boolean, listener: TokenRetrieveListener) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            currentUser.getIdToken(refresh).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener.onTokenRetrieved(success = true, token = task.result!!.token!!)
                } else {
                    listener.onTokenRetrieved(success = false, token = "")
                }
            }
        } else {
            listener.onTokenRetrieved(false, "")
        }
    }

    private fun startListeningForIdTokenChanges() {
        mIdTokenListener = FirebaseAuth.IdTokenListener { firebaseAuth ->
            Log.d(TAG, "inside onIdTokenChanged")
            firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                retrieveIdToken(false)
            }
        }
        FirebaseAuth.getInstance().addIdTokenListener(mIdTokenListener!!)
    }

    fun getFirebaseUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun getFirebaseUserId(): String? {
        return getFirebaseUser()?.uid
    }

    fun getFirebaseAuthToken(): String? {
        return SharedPreferenceManager.getFirebaseAuthToken()
    }

    fun isUserLoggedIn(): Boolean {
        return !(FirebaseAuth.getInstance().currentUser == null || FirebaseAuth.getInstance().currentUser!!.isAnonymous)
    }

    fun isAnonymousLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null && FirebaseAuth.getInstance().currentUser!!.isAnonymous
    }

    @SuppressLint("CheckResult")
    fun registerFCMToken() {
        val userId: String? = getFirebaseUserId()
//        if (userId == null || TextUtils.isEmpty(userId)) {
//            return
//        }
        if (SharedPreferenceManager.isFCMRegisteredOnserver(userId)) {
            return
        }
        val appInstanceId = FirebaseInstanceId.getInstance().id
        Log.d(TAG, "FCM InstanceId - $appInstanceId")
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val fcmToken = instanceIdResult.token
            Log.d(TAG, "FCM token - $fcmToken")
            if (!TextUtils.isEmpty(fcmToken)) {
                getAppDisposable().add(DDApplication.getInstance().getAPIService().registerFCM(BuildConfig.APPLICATION_ID, "android", appInstanceId,
                        BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME, fcmToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : CallbackWrapper<Response<GenericResponse>>() {
                            override fun onSuccess(t: Response<GenericResponse>) {
                                if (t.isSuccessful && !userId.isNullOrEmpty()) {
                                    SharedPreferenceManager.setFCMRegisteredOnserver(userId)
                                }
                            }

                            override fun onFailure(code: Int, message: String) {

                            }
                        }))
            }
        }

    }

    @SuppressLint("CheckResult")
    private fun unregisterFCMToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val fcmToken = instanceIdResult.token
            if (!TextUtils.isEmpty(fcmToken)) {
                getAppDisposable().add(
                        DDApplication.getInstance().getAPIService().unregisterFCM(fcmToken!!)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(object : CallbackWrapper<Response<String>>() {
                                    override fun onSuccess(t: Response<String>) {
                                        if (t.isSuccessful) {
                                            Log.d(TAG, "Call unregister FCM response - " + t.code())
                                        }
                                    }

                                    override fun onFailure(code: Int, message: String) {
                                        Log.d(TAG, "Call unregister FCM error - $code")
                                    }
                                })
                )
            }
        }
    }

    fun finalize() {
        try {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener!!)
            FirebaseAuth.getInstance().removeIdTokenListener(mIdTokenListener!!)
        } catch (e: Exception) {

        }
        firebaseTokenRefreshHandler?.removeCallbacks(firebaseTokenRefreshRunnable)
        firebaseTokenRefreshHandlerThread?.quit()
    }

    private fun getAppDisposable(): AppDisposable {
        if (appDisposable == null) {
            appDisposable = AppDisposable()
        }
        return appDisposable as AppDisposable
    }

    interface TokenRetrieveListener {
        fun onTokenRetrieved(success: Boolean, token: String)
    }
}