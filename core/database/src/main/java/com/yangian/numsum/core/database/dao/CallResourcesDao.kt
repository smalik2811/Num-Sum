package com.yangian.numsum.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yangian.numsum.core.constant.Constant.CALL_TABLE_NAME
import com.yangian.numsum.core.database.model.NumSumCallEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CallResourcesDao {

    @Query("SELECT * FROM $CALL_TABLE_NAME ORDER BY id DESC")
    fun getCalls(): Flow<List<NumSumCallEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCalls(calls: List<NumSumCallEntity>)

    @Query("DELETE FROM $CALL_TABLE_NAME")
    suspend fun deleteCalls()
}