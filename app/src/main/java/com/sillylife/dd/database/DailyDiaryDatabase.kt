package com.sillylife.dd.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sillylife.dd.constants.Constants
import com.sillylife.dd.database.dao.MixedItemDataDao
import com.sillylife.dd.database.entities.MixedItemDataEntity


@Database(entities = arrayOf(MixedItemDataEntity::class), version = 2)
abstract class DailyDiaryDatabase : RoomDatabase() {

    abstract fun mixedItemDataDao(): MixedItemDataDao

    companion object {

        private var INSTANCE: DailyDiaryDatabase? = null

        fun getInstance(context: Context): DailyDiaryDatabase? {
            if (INSTANCE == null) {
                synchronized(DailyDiaryDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            DailyDiaryDatabase::class.java,
                            Constants.DAILY_DIARY_DATABASE)
                            .addMigrations(MIGRATION_1_TO_2)
                            .allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }


        private val MIGRATION_1_TO_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    database.execSQL(
                            "CREATE TABLE IF NOT EXISTS `mixed_item`(" +
                                    "`id` INTEGER NOT NULL," +
                                    "`title` TEXT NOT NULL," +
                                    "`type` TEXT NOT NULL," +
                                    "PRIMARY KEY(`id`))"
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
