package com.yangian.numsum.core.model

import android.icu.util.Calendar

data class CallResource(
    val id: Long,
    val name: String?,
    val number: String,
    val timestamp: Long,
    val duration: Long,
    val type: Int,
)

fun CallResource.getDateString(): String {
    val calendarInstance = Calendar.getInstance()
    calendarInstance.timeInMillis = timestamp

    val monthName = when (calendarInstance.get(Calendar.MONTH)) {
        0 -> "Jan"
        1 -> "Feb"
        2 -> "Mar"
        3 -> "Apr"
        4 -> "May"
        5 -> "Jun"
        6 -> "Jul"
        7 -> "Aug"
        8 -> "Sep"
        9 -> "Oct"
        10 -> "Nov"
        11 -> "Dec"
        else -> ""
    }
    return "$monthName " +
            "${calendarInstance.get(Calendar.DAY_OF_MONTH)}, " +
            "${calendarInstance.get(Calendar.YEAR)}"
}

fun CallResource.getDurationString(): String {
    val hour = duration / 3600
    val minute = (duration % 3600) / 60
    val second = duration % 60

    return if (hour > 0) {
        "${hour}h ${minute}m ${second}s"
    } else if (minute > 0) {
        "${minute}m ${second}s"
    } else {
        "${second}s"
    }
}


//INCOMING_TYPE - 1 : Call log type for calls received by the user.
//OUTGOING_TYPE - 2 : Call log type for calls made by the user.
//MISSED_TYPE - 3 : Call log type for calls missed by the user.
//VOICEMAIL_TYPE - 4 : Call log type for calls made with a voicemail.
//REJECTED_TYPE - 5 : Call log type for calls rejected by the user.
//BLOCKED_TYPE - 6 : Call log type for calls blocked automatically.
//ANSWERED_EXTERNALLY_TYPE - 7 : Call log type for a call which was answered on another device. Used in situations where a call rings on multiple devices simultaneously and it ended up being answered on a device other than the current one.