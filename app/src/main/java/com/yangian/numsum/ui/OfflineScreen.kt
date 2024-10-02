package com.yangian.numsum.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yangian.numsum.R
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.icon.CloudOffIcon
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme

@Composable
fun OfflineScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                dimensionResource(
                    com.yangian.numsum.feature.onboard.R.dimen.padding_medium
                )
            ),
    ) {
        Icon(
            CloudOffIcon,
            stringResource(R.string.not_internet_connection),
            modifier = Modifier
                .size(dimensionResource(com.yangian.numsum.feature.onboard.R.dimen.icon_size_large))
        )

        Spacer(
            modifier = Modifier.height(
                dimensionResource(
                    com.yangian.numsum.feature.onboard.R.dimen.padding_medium
                )
            )
        )

        Text("You are offline. Check your connection.")
    }
}

@Preview
@Composable
private fun OfflineScreenPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
            OfflineScreen()
        }
    }
}