package com.skeletonkotlin.e_cigarette.data.room.dao

import androidx.room.*
import com.skeletonkotlin.e_cigarette.data.room.entity.DoorEntity

@Dao
interface DoorDao {
    @Query("SELECT * FROM door_table")
    fun getAll(): List<DoorEntity>

//    @Query("SELECT * FROM log_table WHERE title LIKE :title")
//    fun findByTitle(title: String): TodoEntity

    @Insert
    fun insertAll(vararg doorEntity: DoorEntity)

    @Delete
    fun delete(doorEntity: DoorEntity)

    @Update
    fun update(vararg doorEntity: DoorEntity)
}