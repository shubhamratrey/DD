package com.sillylife.dd.services.sharedpreference

import android.text.TextUtils


object SharedPreferenceManager {

    val sharedPreferences = SharedPreferences

    private val TAG = SharedPreferenceManager::class.java.simpleName

    private const val FIREBASE_AUTH_TOKEN = "firebase_auth_token"
    private const val FCM_REGISTERED_USER = "fcm_registered_user"
    private const val USER = "user"
    private const val CART_LIST = "cart_list"


    fun storeFirebaseAuthToken(firebaseAuthToken: String) {
        SharedPreferences.setString(FIREBASE_AUTH_TOKEN, firebaseAuthToken)
    }

    fun getFirebaseAuthToken(): String {
        return SharedPreferences.getString(FIREBASE_AUTH_TOKEN, "")!!
    }


    fun isFCMRegisteredOnserver(userId: String?): Boolean {
        return if (userId == null || TextUtils.isEmpty(userId)) {
            false
        } else SharedPreferences.getBoolean(FCM_REGISTERED_USER + userId, false)
    }

    fun setFCMRegisteredOnserver(userId: String) {
        SharedPreferences.setBoolean(FCM_REGISTERED_USER + userId, true)
    }

//    fun setUser(user: User) {
//        SharedPreferences.setString(USER, Gson().toJson(user))
//    }
//
//    fun getUser(): User? {
//        val raw: String = SharedPreferences.getString(USER, "")!!
//        if (!CommonUtil.textIsEmpty(raw)) {
//            return Gson().fromJson(raw, User::class.java)
//        }
//        return null
//    }

//    fun setCartList(list: ArrayList<Cart>) {
//        SharedPreferences.setString(CART_LIST, Gson().toJson(list))
//    }
//
//    fun getCartList(): ArrayList<Cart> {
//        var list = ArrayList<Cart>()
//        val raw = SharedPreferences.getString(CART_LIST, "")
//        if (!CommonUtil.textIsEmpty(raw)) {
//            list = Gson().fromJson(SharedPreferences.getString(CART_LIST, ""), object : TypeToken<ArrayList<Cart>>() {}.type)
//        }
//        return list
//    }

}