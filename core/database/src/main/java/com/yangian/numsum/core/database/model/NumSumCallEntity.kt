package com.yangian.numsum.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yangian.numsum.core.constant.Constant.CALL_TABLE_NAME
import com.yangian.numsum.core.model.CallResource

@Entity(
    tableName = CALL_TABLE_NAME
)
data class NumSumCallEntity(
    @PrimaryKey
    val id: Long,
    val name: String?,
    val number: String,
    val date: Long,
    val duration: Long,
    val type: Int,
)

fun NumSumCallEntity.asExternalModel() = CallResource(
    id = id,
    name = name,
    number = number,
    timestamp = date,
    duration = duration,
    type = type,
)