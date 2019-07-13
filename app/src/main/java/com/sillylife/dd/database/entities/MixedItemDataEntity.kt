package com.sillylife.dd.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mixed_item")
data class MixedItemDataEntity(

        @PrimaryKey(autoGenerate = true)
        val id: Int,

        @ColumnInfo(name = "type")
        val type: String,

        @ColumnInfo(name = "title")
        val title: String = ""
)