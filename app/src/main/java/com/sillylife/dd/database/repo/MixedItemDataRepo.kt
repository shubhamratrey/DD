package com.sillylife.dd.database.repo

import android.app.Application
import com.sillylife.dd.database.DailyDiaryDatabase
import com.sillylife.dd.database.DatabaseTask
import com.sillylife.dd.enums.DatabaseTaskType
import com.sillylife.dd.database.dao.MixedItemDataDao
import com.sillylife.dd.database.entities.MixedItemDataEntity

class MixedItemDataRepo(application: Application) {
    private var dao: MixedItemDataDao? = null

    init {
        val kukuFMDatabase = DailyDiaryDatabase.getInstance(application)
        dao = kukuFMDatabase?.mixedItemDataDao()
    }

    fun getById(channelId: Int): MixedItemDataEntity?? {
        return dao?.getById(channelId)
    }

    fun getLastInserted(): MixedItemDataEntity? {
        return dao?.getLastInserted()
    }

    fun insert(entity: MixedItemDataEntity) {
        dao?.insert(entity)
    }

    fun databaseTask(taskType: DatabaseTaskType, entity: MixedItemDataEntity, listener: (DatabaseTaskType, Any?) -> Unit) {
        DatabaseTask(taskType, dao) { listener(taskType, it) }.execute(entity)
    }
}
