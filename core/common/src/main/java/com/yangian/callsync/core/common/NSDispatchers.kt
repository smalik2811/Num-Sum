package com.yangian.callsync.core.common

import androidx.annotation.Keep
import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME


@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val csDispatchers: NSDispatchers)

enum class NSDispatchers {
    Default,
    IO,
}