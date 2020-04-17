package com.sillylife.dd.database.dao

import androidx.room.*
import com.sillylife.dd.database.entities.MixedItemDataEntity

@Dao
interface MixedItemDataDao {

    @Query("SELECT * FROM mixed_item WHERE id = :id")
    fun getById(id: Int): MixedItemDataEntity

    /*@Query("SELECT * FROM channel WHERE is_playing = 1")
    fun getRecentlyPlayedChannel(): ChannelEntity?*/

    @Query("SELECT * FROM mixed_item order by id desc limit :limit")
    fun getLastInserted(limit:Int): MixedItemDataEntity

    @Delete
    fun delete(vararg channel: MixedItemDataEntity)

    @Update
    fun update(vararg channel: MixedItemDataEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg channel: MixedItemDataEntity)

}
