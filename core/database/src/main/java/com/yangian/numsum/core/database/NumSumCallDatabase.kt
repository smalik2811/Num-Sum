package com.yangian.numsum.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yangian.numsum.core.database.dao.CallResourcesDao
import com.yangian.numsum.core.database.model.NumSumCallEntity

@Database(
    entities = [
        NumSumCallEntity::class,
    ],
    version = 1,
    autoMigrations = [],
    exportSchema = true,
)
internal abstract class NumSumCallDatabase : RoomDatabase() {
    abstract fun callResourceDao(): CallResourcesDao
}