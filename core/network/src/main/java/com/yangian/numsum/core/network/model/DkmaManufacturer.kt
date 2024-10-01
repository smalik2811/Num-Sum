package com.yangian.numsum.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class DkmaManufacturer(
    val explanation: String,
    val user_solution: String,
)