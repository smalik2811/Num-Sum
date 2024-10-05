package com.yangian.numsum.core.data.repository

import androidx.annotation.Keep
import com.yangian.numsum.core.data.model.asEntity
import com.yangian.numsum.core.database.dao.CallResourcesDao
import com.yangian.numsum.core.database.model.NumSumCallEntity
import com.yangian.numsum.core.database.model.asExternalModel
import com.yangian.numsum.core.model.CallResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultCallResourceRepository @Inject constructor(
    private val callResourcesDao: CallResourcesDao
) : CallResourceRepository {

    override fun getCalls(): Flow<List<CallResource>> {
        return callResourcesDao.getCalls()
            .map { it.map(NumSumCallEntity::asExternalModel) }
    }

    override suspend fun addCalls(calls: List<CallResource>) {
        callResourcesDao.insertCalls(
            calls.map {
                it.asEntity(
                )
            }
        )
    }

    override suspend fun deleteCalls() {
        callResourcesDao.deleteCalls()
    }
}