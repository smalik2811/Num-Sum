package com.yangian.numsum.feature.calculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.ads.nativead.NativeAd
import com.yangian.numsum.core.designsystem.BuildConfig
import com.yangian.numsum.core.designsystem.component.CalculatorButton
import com.yangian.numsum.core.designsystem.component.CalculatorIconButton
import com.yangian.numsum.core.designsystem.component.admob.AdMobBannerExpanded
import com.yangian.numsum.core.designsystem.component.admob.CallNativeAd
import com.yangian.numsum.core.designsystem.component.admob.loadNativeAd
import com.yangian.numsum.core.designsystem.icon.BackspaceIcon
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.feature.calculator.CalculatorViewModel

@Composable
fun CalculatorRouteExpanded(
    calculatorViewModel: CalculatorViewModel = hiltViewModel(),
    navigateToLockedScreen: () -> Unit,
) {

    val calculatorUiState by calculatorViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background,
            )
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
                .padding(start = 12.dp, end = 12.dp)
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.length < 20) {
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
                    if (calculatorUiState.appUnlocked) {
                        navigateToLockedScreen()
                    }
                }
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    apiLevel = 33, device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Composable
private fun CalculatorRouteExpandedPreview() {
    NumSumAppTheme {
        CalculatorRouteExpanded(
            navigateToLockedScreen = {}
        )
    }
}