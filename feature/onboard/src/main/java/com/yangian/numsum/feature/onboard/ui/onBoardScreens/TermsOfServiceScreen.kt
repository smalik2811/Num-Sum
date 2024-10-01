package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.feature.onboard.R
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun TermsOfServiceScreen(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
    ) {
        val termsOfServiceText = stringResource(R.string.terms_of_services)
        MarkdownText(
            markdown = termsOfServiceText,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}

@Preview
@Composable
private fun TermsOfServiceScreenPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
            TermsOfServiceScreen()
        }
    }
}