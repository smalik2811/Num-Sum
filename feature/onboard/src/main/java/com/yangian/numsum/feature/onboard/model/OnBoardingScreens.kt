package com.yangian.numsum.feature.onboard.model

import androidx.annotation.StringRes
import com.yangian.numsum.feature.onboard.R

enum class OnBoardingScreens(
    @StringRes
    val title: Int
) {
    TermsOfService(R.string.tos_title),
    Welcome(R.string.welcome_title),
    Connection1(R.string.connection_title),
    Connection2(R.string.connection_title)
}