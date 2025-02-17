package com.yangian.numsum.feature.calculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.ads.nativead.NativeAd
import com.yangian.numsum.core.designsystem.component.CalculatorButton
import com.yangian.numsum.core.designsystem.component.CalculatorIconButton
import com.yangian.numsum.core.designsystem.component.admob.CallNativeAd
import com.yangian.numsum.core.designsystem.component.admob.loadNativeAd
import com.yangian.numsum.core.designsystem.icon.BackspaceIcon
import com.yangian.numsum.core.designsystem.theme.AppTheme
import com.yangian.numsum.feature.calculator.BuildConfig
import com.yangian.numsum.feature.calculator.CalculatorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun CalculatorRouteMedium(
    calculatorViewModel: CalculatorViewModel = hiltViewModel(),
    navigateToLockedScreen: () -> Unit,
) {

    val calculatorUiState by calculatorViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    LaunchedEffect(key1 = null) {
        launch(Dispatchers.Main) {
            while (isActive) { // isActive checks if the coroutine is still active
                loadNativeAd(context, BuildConfig.NativeAdUnitId) {
                    nativeAd = it
                }
                delay(30_000) // Load a new ad every 30 seconds
            }
        }
    }

    if (calculatorUiState.appUnlocked) {
        calculatorViewModel.clearCalculator()
        navigateToLockedScreen()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background,
            )
            .consumeWindowInsets(PaddingValues(12.dp))
            .windowInsetsPadding(WindowInsets.navigationBars)
            .windowInsetsPadding(WindowInsets.safeContent)
            .windowInsetsPadding(WindowInsets.statusBars),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
                .padding(start = 12.dp, end = 12.dp)
        ) {
            nativeAd?.let {
                CallNativeAd(
                    it,
                    Modifier
                        .fillMaxHeight()
                        .weight(4f)
                        .padding(
                            start = dimensionResource(com.yangian.numsum.core.designsystem.R.dimen.padding_tiny),
                            end = dimensionResource(com.yangian.numsum.core.designsystem.R.dimen.padding_tiny)
                        )
                )
            }


            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(6f)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
            ) {
                Text(
                    text = calculatorUiState.expression,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.W500,
                    maxLines = 3,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(
                            bottom = 8.dp,
                            end = 8.dp,
                            start = 8.dp
                        )
                )

                Text(
                    text = calculatorUiState.result,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(
                        bottom = 8.dp,
                        end = 24.dp
                    )
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 24.dp, end = 24.dp)
        ) {
            CalculatorButton(
                text = "7",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendNumber('7')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "8",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendNumber('8')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "9",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendNumber('9')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "÷",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendOperator('÷')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "AC",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.75f),
                contentColor = MaterialTheme.colorScheme.onTertiary,
                onClick = { calculatorViewModel.clearCalculator() }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 24.dp, end = 24.dp)
        ) {
            CalculatorButton(
                text = "4",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendNumber('4')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "5",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendNumber('5')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "6",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendNumber('6')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "×",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendOperator('×')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "(  )",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendParenthesis()
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 24.dp, end = 24.dp)
        ) {
            CalculatorButton(
                text = "1",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendNumber('1')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "2",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendNumber('2')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "3",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendNumber('3')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "-",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendOperator('-')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "%",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendOperator('%')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 24.dp, end = 24.dp)
        ) {
            CalculatorButton(
                text = "0",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendNumber('0')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "•",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendDecimal()
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorIconButton(
                icon = BackspaceIcon,
                contentDescription = "Delete Character",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    calculatorViewModel.deleteLastCharacter()
                    calculatorViewModel.evaluateExpressionCompact()
                }
            )

            CalculatorButton(
                text = "+",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                    if (calculatorUiState.length < 35) {
                        calculatorViewModel.appendOperator('+')
                        calculatorViewModel.evaluateExpressionCompact()
                    }
                }
            )

            CalculatorButton(
                text = "=",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                onClick = {
                    calculatorViewModel.evaluateExpressionCompact()
                    calculatorViewModel.prepareResult()
                }
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    apiLevel = 33
)
@Composable
private fun CalculatorRouteMediumPreview() {
    AppTheme {
        CalculatorRouteMedium(
            navigateToLockedScreen = {}
        )
    }
}