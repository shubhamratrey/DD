package com.sillylife.dd.services.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import com.sillylife.dd.BuildConfig
import com.sillylife.dd.DDApplication

object SharedPreferences {

    private val PREF_NAME = BuildConfig.APPLICATION_ID

    private val prefs: SharedPreferences =
        DDApplication.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * Clears all data in SharedPreferences
     */
    fun clearPrefs() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * Remove saved sharedpreference key and value
     */
    fun removeKey(key: String) {
        prefs.edit().remove(key).apply()
    }

    /**
     * Checks if this key exist in sharedpreference
     */
    fun containsKey(key: String): Boolean {
        return prefs.contains(key)
    }

    /**
     * Returns value as string for given specified key
     * and if not exist will return default passed value
     */
    fun getString(key: String, defValue: String?): String? {
        return prefs.getString(key, defValue)
    }

    fun setString(key: String, value: String) {
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * Returns value as int for given specified key
     * and if not exist will return default passed value
     */
    fun getInt(key: String, defValue: Int): Int {
        return prefs.getInt(key, defValue)
    }


    fun setInt(key: String, value: Int) {
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /**
     * Returns value as long for given specified key
     * and if not exist will return default passed value
     */
    fun getLong(key: String, defValue: Long): Long {
        return prefs.getLong(key, defValue)
    }

    fun setLong(key: String, value: Long) {
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    /**
     * Returns value as boolean for given specified key
     * and if not exist will return default passed value
     */
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * Returns value as float for given specified key
     * and if not exist will return default passed value
     */
    fun getFloat(key: String, defValue: Float): Float {
        return prefs.getFloat(key, defValue)
    }

    fun setFloat(key: String, value: Float?) {
        val editor = prefs.edit()
        editor.putFloat(key, value!!)
        editor.apply()
    }

}