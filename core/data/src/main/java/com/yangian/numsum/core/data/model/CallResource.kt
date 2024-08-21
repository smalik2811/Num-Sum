package com.yangian.numsum.core.data.model

import com.yangian.numsum.core.database.model.NumSumCallEntity
import com.yangian.numsum.core.model.CallResource

fun CallResource.asEntity() = NumSumCallEntity(
    id = id,
    name = name,
    number = number,
    date = timestamp,
    duration = duration,
    type = type,
)