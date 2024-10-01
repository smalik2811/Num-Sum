package com.yangian.numsum.core.data.repository

import com.yangian.numsum.core.model.CallResource
import kotlinx.coroutines.flow.Flow

interface CallResourceRepository {

    fun getCalls(): Flow<List<CallResource>>

    suspend fun addCalls(calls: List<CallResource>)

    suspend fun deleteCalls()


}