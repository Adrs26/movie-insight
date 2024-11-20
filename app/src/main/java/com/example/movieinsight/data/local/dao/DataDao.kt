package com.example.movieinsight.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieinsight.data.local.entity.Data

@Dao
interface DataDao {
    @Query("SELECT * FROM data WHERE dataType = :type")
    suspend fun getDataByType(type: String): List<Data>

    @Query("SELECT COUNT(*) FROM data WHERE id = :id")
    suspend fun isDataExist(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(data: Data)

    @Query("DELETE FROM data WHERE id = :id")
    suspend fun deleteData(id: Int)
}