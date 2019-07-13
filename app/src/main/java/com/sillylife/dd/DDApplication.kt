package com.sillylife.dd

import android.app.Application
import com.facebook.stetho.Stetho
import com.sillylife.dd.database.DailyDiaryDatabase

class DDApplication : Application() {

    @Volatile
    private var mKukuFMDatabase: DailyDiaryDatabase? = null

    companion object {
        @Volatile
        private var kukuFMApplication: DDApplication? = null

        @Synchronized
        fun getInstance(): DDApplication {
            return kukuFMApplication!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        kukuFMApplication = this@DDApplication
        Stetho.initializeWithDefaults(this)

    }

    @Synchronized
    fun getDailyDiaryDatabase(): DailyDiaryDatabase? {
        if (mKukuFMDatabase == null) {
            mKukuFMDatabase = DailyDiaryDatabase.getInstance(this)
        }
        return mKukuFMDatabase
    }

}