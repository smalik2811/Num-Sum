package com.yangian.numsum.core.designsystem.component

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.window.core.layout.WindowWidthSizeClass
import com.yangian.numsum.core.designsystem.MultiDevicePreview
import com.yangian.numsum.core.designsystem.R
import com.yangian.numsum.core.designsystem.theme.AppTheme
import com.yangian.numsum.core.network.model.DkmaManufacturer

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DkmaScreenWebViewCard(
    headingText: String,
    webViewHtmlContent: String,
    isVisible: Boolean,
    alterVisibility: () -> Unit,
    modifier: Modifier = Modifier
) {
    val htmlCode: String = stringResource(
        R.string.html_boiler_plate,
        webViewHtmlContent
    )

    OutlinedCard(
        modifier = modifier,
        shape = ShapeDefaults.Medium
    ) {
        Column(
            modifier = Modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(
                        top = dimensionResource(R.dimen.padding_tiny),
                        bottom = dimensionResource(R.dimen.padding_tiny),
                        start = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    )
                    .clickable { alterVisibility() },
            ) {
                Text(
                    text = headingText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = alterVisibility,
                    colors = IconButtonDefaults.iconButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = when (isVisible) {
                            false -> Icons.Filled.ExpandMore
                            true -> Icons.Filled.ExpandLess
                        },
                        contentDescription = when (isVisible) {
                            true -> stringResource(R.string.hide)
                            false -> stringResource(R.string.show)
                        },
                    )
                }
            }

            AnimatedVisibility(isVisible) {

                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            loadData(
                                htmlCode,
                                "text/html",
                                "Utf-8"
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun CompactDkmaView(
    dkmaManufacturer: DkmaManufacturer,
    isIssueVisible: Boolean,
    isSolutionVisible: Boolean,
    alterIssueVisibility: () -> Unit,
    alterSolutionVisibility: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val localPaddingValue = (dimensionResource(R.dimen.padding_medium))

    OutlinedCard(
        modifier = modifier
    ) {

        Text(
            text = stringResource(R.string.potential_issues),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(
                start = localPaddingValue,
                top = localPaddingValue,
                end = localPaddingValue
            )
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_tiny)))

        Text(
            text = stringResource(R.string.potential_issues_description),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(
                start = localPaddingValue,
                end = localPaddingValue
            )
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

        DkmaScreenWebViewCard(
            headingText = stringResource(R.string.check_issues),
            webViewHtmlContent = dkmaManufacturer.explanation,
            isVisible = isIssueVisible,
            alterVisibility = alterIssueVisibility,
            modifier = modifier.padding(
                start = localPaddingValue,
                end = localPaddingValue,
                bottom = localPaddingValue
            )
        )
    }

    Spacer(
        modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_large))
    )

    OutlinedCard(
        modifier = modifier
    ) {

        Text(
            text = stringResource(R.string.potential_solutions),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(
                    start = localPaddingValue,
                    end = localPaddingValue,
                    top = localPaddingValue
                )
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_tiny)))

        Text(
            text = stringResource(R.string.potential_solutions_description),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(
                start = localPaddingValue,
                end = localPaddingValue
            )
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        DkmaScreenWebViewCard(
            headingText = stringResource(R.string.check_solutions),
            webViewHtmlContent = dkmaManufacturer.user_solution,
            isVisible = isSolutionVisible,
            alterVisibility = alterSolutionVisibility,
            modifier = modifier.padding(
                start = localPaddingValue,
                end = localPaddingValue,
                bottom = localPaddingValue
            )
        )
    }
}

@Composable
fun RowScope.ExpandedDkmaView(
    dkmaManufacturer: DkmaManufacturer,
    isIssueVisible: Boolean,
    isSolutionVisible: Boolean,
    alterIssueVisibility: () -> Unit,
    alterSolutionVisibility: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier.weight(1f)
    ) {

        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {

            Text(
                text = stringResource(R.string.potential_issues),
                style = MaterialTheme.typography.headlineLarge,
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_tiny)))

            Text(
                text = stringResource(R.string.potential_issues_description),
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            DkmaScreenWebViewCard(
                headingText = stringResource(R.string.check_issues),
                webViewHtmlContent = dkmaManufacturer.explanation,
                isVisible = isIssueVisible,
                alterVisibility = alterIssueVisibility
            )
        }
    }

    Spacer(
        modifier = Modifier.width(dimensionResource(R.dimen.padding_extra_large))
    )

    OutlinedCard(
        modifier = modifier.weight(1f)
    ) {

        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) {

            Text(
                text = stringResource(R.string.potential_solutions),
                style = MaterialTheme.typography.headlineLarge,
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_tiny)))

            Text(
                text = stringResource(R.string.potential_solutions_description),
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            DkmaScreenWebViewCard(
                headingText = stringResource(R.string.check_solutions),
                webViewHtmlContent = dkmaManufacturer.user_solution,
                isVisible = isSolutionVisible,
                alterVisibility = alterSolutionVisibility
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DkmaView(
    dkmaManufacturer: DkmaManufacturer,
    isIssueVisible: Boolean,
    isSolutionVisible: Boolean,
    alterIssueVisibility: () -> Unit,
    alterSolutionVisibility: () -> Unit,
    modifier: Modifier = Modifier
) {

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.EXPANDED -> {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
            ) {
                ExpandedDkmaView(
                    dkmaManufacturer,
                    isIssueVisible,
                    isSolutionVisible,
                    alterIssueVisibility,
                    alterSolutionVisibility,
                )
            }
        }

        else -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = modifier
            ) {
                CompactDkmaView(
                    dkmaManufacturer,
                    isIssueVisible,
                    isSolutionVisible,
                    alterIssueVisibility,
                    alterSolutionVisibility,
                )
            }
        }
    }
}

@MultiDevicePreview
@Composable
private fun DkmaViewPreview() {
    val mockData = DkmaManufacturer(
        explanation = stringResource(R.string.dkma_dummy_explanation),
        user_solution = stringResource(R.string.dkma_dummy_user_solution),
    )

    var isIssueVisible by remember { mutableStateOf(false) }
    var isSolutionVisible by remember { mutableStateOf(false) }

    AppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            DkmaView(
                mockData,
                isIssueVisible = isIssueVisible,
                isSolutionVisible = isSolutionVisible,
                alterIssueVisibility = { isIssueVisible = !isIssueVisible },
                alterSolutionVisibility = { isSolutionVisible = !isSolutionVisible },
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(28.0.dp))

            )
        }
    }
}