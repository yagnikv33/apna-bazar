package com.skeletonkotlin.e_cigarette.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "door_table")
data class DoorEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "uid") var uId: Int,
    @ColumnInfo(name = "door_name") var doorName: String
)