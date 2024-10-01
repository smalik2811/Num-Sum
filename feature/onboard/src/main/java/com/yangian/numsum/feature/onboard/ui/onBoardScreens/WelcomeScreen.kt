package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.feature.onboard.R

@Composable
fun WelcomeScreen(
    createFirebaseAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {

    createFirebaseAccount()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
    ) {

        Spacer(modifier = Modifier.weight(1f))

        // App icon
        Box(
            modifier = Modifier.shadow(
                elevation = dimensionResource(R.dimen.elevation_level_2),
                shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_extra_large))
            )
        ) {
            Image(
                painter = painterResource(R.mipmap.launcher_background),
                contentDescription = stringResource(R.string.num_sum_app_logo),
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            dimensionResource(
                                R.dimen.corner_radius_extra_large
                            )
                        )
                    )
            )

            Image(
                painter = painterResource(R.mipmap.num_sum_launcher_foreground),
                contentDescription = stringResource(R.string.num_sum_app_logo),
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            dimensionResource(
                                R.dimen.corner_radius_extra_large
                            )
                        )
                    )
            )
        }

        Spacer(
            modifier = Modifier.weight(0.2f)
        )

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(stringResource(R.string.call_sync_name))
                }
                append(stringResource(R.string.space_string))
                append(stringResource(R.string.welcome_description))
            },
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
            WelcomeScreen({})
        }
    }
}