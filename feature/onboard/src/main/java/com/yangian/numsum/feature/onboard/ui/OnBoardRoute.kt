package com.yangian.numsum.feature.onboard.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.icon.ArrowBackIcon
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.feature.onboard.OnBoardViewModel
import com.yangian.numsum.feature.onboard.R
import com.yangian.numsum.feature.onboard.model.OnBoardingScreens
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.ConnectionScreen1
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.ConnectionScreen2
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.DkmaScreen
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.TermsOfServiceScreen
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.WelcomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardRoute(
    appContext: Context,
    modifier: Modifier = Modifier,
    navigateToCalculator: () -> Unit = {},
    navigateToTemporary: () -> Unit = {},
    onBoardViewModel: OnBoardViewModel = hiltViewModel(),
) {

    val currentScreen by onBoardViewModel.currentScreen.collectAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            currentScreen.title
                        ),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visibleState = MutableTransitionState(currentScreen != OnBoardingScreens.TermsOfService && currentScreen != OnBoardingScreens.Connection2),
                        enter = slideInHorizontally(),
                        exit = slideOutHorizontally()
                    ) {
                        IconButton(
                            onClick = onBoardViewModel::navigateToPreviousScreen
                        ) {
                            Icon(
                                imageVector = ArrowBackIcon,
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    }
                },
            )
        },
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            OnBoardingScreen(
                onBoardViewModel = onBoardViewModel,
                appContext = appContext,
                navigateToCalculator = navigateToCalculator,
                navigateToTemporary = navigateToTemporary,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            if (currentScreen != OnBoardingScreens.Connection2) {
                if (currentScreen == OnBoardingScreens.Connection1) {
                    if (onBoardViewModel.handShakeUIState is com.yangian.numsum.feature.onboard.HandShakeUI.Success) {
                        Spacer(
                            modifier = Modifier.height(dimensionResource(R.dimen.padding_small))
                        )

                        Button(
                            onClick = onBoardViewModel::navigateToNextScreen,
                        ) {
                            Text(
                                text = stringResource(id = R.string.proceed)
                            )
                        }
                    }
                } else {
                    Spacer(
                        modifier = Modifier.height(dimensionResource(R.dimen.padding_small))
                    )

                    Button(
                        onClick = onBoardViewModel::navigateToNextScreen,
                    ) {
                        Text(
                            text =
                            if (currentScreen == OnBoardingScreens.TermsOfService) {
                                stringResource(R.string.agree_and_proceed)
                            } else {
                                stringResource(id = R.string.proceed)
                            },
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.padding_small))
        )
    }
}

@Composable
fun OnBoardingScreen(
    onBoardViewModel: OnBoardViewModel,
    appContext: Context,
    navigateToCalculator: () -> Unit,
    navigateToTemporary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentScreen by onBoardViewModel.currentScreen.collectAsState()

    when (currentScreen) {
        OnBoardingScreens.TermsOfService -> TermsOfServiceScreen(modifier)
        OnBoardingScreens.Welcome -> WelcomeScreen(
            onBoardViewModel::createFirebaseAccount,
            modifier = modifier,
        )

        OnBoardingScreens.DkmaScreen -> DkmaScreen(
            onBoardViewModel.dkmaUiState,
            onBoardViewModel.isIssueVisible,
            onBoardViewModel.isSolutionVisible,
            onBoardViewModel::alterIssueVisibility,
            onBoardViewModel::alterSolutionVisibility,
            onBoardViewModel::loadDkmaManufacturer,
            modifier
        )

        OnBoardingScreens.Connection1 -> ConnectionScreen1(
            handShakeUIState = onBoardViewModel.handShakeUIState,
            prepareQrCode = onBoardViewModel::prepareQrCode,
            turnHandShakeUICold = onBoardViewModel::turnHandShakeUICold,
            modifier
        )

        OnBoardingScreens.Connection2 -> ConnectionScreen2(
            onBoardViewModel::validateReceiver,
            navigateToTemporary,
            onBoardViewModel::navigateToPreviousScreen,
            modifier
        )
    }
}

@Preview
@Composable
private fun OnBoardPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
            OnBoardRoute(
                appContext = LocalContext.current,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}