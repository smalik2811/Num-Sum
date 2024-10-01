package com.yangian.numsum.core.network.retrofit

import com.yangian.numsum.core.network.model.DkmaManufacturer

interface DkmaNetworkDataSource {
    suspend fun getDkmaManufacturer(manufacturer: String): DkmaManufacturer
}