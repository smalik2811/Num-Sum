package com.yangian.numsum.feature.onboard.ui

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.icon.ArrowBackIcon
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.feature.onboard.OnBoardViewModel
import com.yangian.numsum.feature.onboard.R
import com.yangian.numsum.feature.onboard.model.OnBoardingScreens
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.ConnectionScreen1
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.ConnectionScreen2
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.TermsOfServiceScreen
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.WelcomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardRoute(
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
                navigateToCalculator = navigateToCalculator,
                navigateToTemporary = navigateToTemporary,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            if (currentScreen != OnBoardingScreens.Connection2) {
                Spacer(
                    modifier = Modifier.height(8.dp)
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

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
    }
}

@Composable
fun OnBoardingScreen(
    onBoardViewModel: OnBoardViewModel,
    navigateToCalculator: () -> Unit = {},
    navigateToTemporary: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val currentScreen by onBoardViewModel.currentScreen.collectAsState()

    when (currentScreen) {
        OnBoardingScreens.TermsOfService -> TermsOfServiceScreen(modifier)
        OnBoardingScreens.Welcome -> WelcomeScreen(
            onBoardViewModel = onBoardViewModel,
            modifier = modifier,
            navigateToCalculator = navigateToCalculator
        )

        OnBoardingScreens.Connection1 -> ConnectionScreen1(onBoardViewModel, modifier)
        OnBoardingScreens.Connection2 -> ConnectionScreen2(
            onBoardViewModel,
            modifier,
            navigateToTemporary
        )
    }
}

@Preview
@Composable
private fun OnBoardPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
            OnBoardRoute(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}