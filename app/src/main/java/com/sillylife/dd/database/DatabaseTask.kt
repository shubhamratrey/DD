package com.sillylife.dd.database

import android.os.AsyncTask
import android.util.Log
import com.sillylife.dd.database.dao.MixedItemDataDao
import com.sillylife.dd.database.entities.MixedItemDataEntity
import com.sillylife.dd.enums.DatabaseTaskType

class DatabaseTask(var taskType: DatabaseTaskType, var dao: Any?, val listener: (Any?) -> Unit) : AsyncTask<Any, Void, Any>() {

    override fun doInBackground(vararg params: Any): Any? {
        var any: Any = params[0]
        when (taskType) {
            DatabaseTaskType.INSERT -> {
                if (any is MixedItemDataEntity) {
                    (dao as MixedItemDataDao).insert(any)
                    return (any as MixedItemDataEntity).id
                }
            }
            DatabaseTaskType.UPDATE -> {
                if (any is MixedItemDataEntity) {
                    (dao as MixedItemDataDao).update(any)
                    return (any as MixedItemDataEntity).id
                }
            }
            DatabaseTaskType.GET -> {
                if (dao is MixedItemDataEntity) {
                    var entity = (dao as MixedItemDataDao).getById((any as MixedItemDataEntity).id!!)
                    Log.d("EpisodeDownloadDao", if (entity == null) "--" else entity.id.toString())
                    return entity
                }
            }
            DatabaseTaskType.DELETE -> {
                if (any is MixedItemDataEntity) {
                    var ede = any as MixedItemDataEntity
                    (dao as MixedItemDataDao).delete(ede)
                    Log.d("EDE Deleted", ede.id.toString())
                }
            }
        }
        return -1
    }


    override fun onPostExecute(result: Any?) {
        var any: Any? = null
        when (taskType) {
            DatabaseTaskType.INSERT -> {
                if (dao is MixedItemDataDao) {
                    any = (dao as MixedItemDataDao).getLastInserted()
                }
            }
            DatabaseTaskType.GET -> {
                if (dao is MixedItemDataDao) {
                    any = result
                    //Log.d("EpisodeDownloadDao", (any as EpisodeDownloadEntity).id.toString())
                }
            }
            DatabaseTaskType.UPDATE -> {
                if (dao is MixedItemDataDao) {
                    any = (dao as MixedItemDataDao).getById(result!! as Int)
                }
            }
        }
        if (listener != null) {
            listener(any)
        }
    }

    override fun onPreExecute() {

    }

    override fun onProgressUpdate(vararg text: Void) {

    }
}