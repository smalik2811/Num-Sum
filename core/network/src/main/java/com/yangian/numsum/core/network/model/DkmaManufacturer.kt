package com.yangian.numsum.core.network.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DkmaManufacturer(
    val explanation: String,
    val user_solution: String,
)