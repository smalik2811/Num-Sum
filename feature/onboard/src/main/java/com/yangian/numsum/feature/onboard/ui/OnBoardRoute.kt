package com.yangian.numsum.feature.onboard.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.yangian.numsum.core.designsystem.component.AppBackground
import com.yangian.numsum.core.designsystem.icon.ArrowBackIcon
import com.yangian.numsum.core.designsystem.theme.AppTheme
import com.yangian.numsum.feature.onboard.DkmaUiState
import com.yangian.numsum.feature.onboard.HandShakeUI
import com.yangian.numsum.feature.onboard.OnBoardViewModel
import com.yangian.numsum.feature.onboard.R
import com.yangian.numsum.feature.onboard.model.OnBoardingScreens
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.Connection1Screen
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.Connection2Screen
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.DkmaScreen
import com.yangian.numsum.feature.onboard.ui.onBoardScreens.WelcomeScreen

@Composable
fun OnBoardRoute(
    modifier: Modifier = Modifier,
    navigateToCalculator: () -> Unit = {},
    navigateToHome: () -> Unit = {},
    onBoardViewModel: OnBoardViewModel = hiltViewModel(),
) {

    val currentScreen by onBoardViewModel.currentScreen.collectAsState()

    OnBoardRoute(
        currentScreen = currentScreen,
        handShakeUI = onBoardViewModel.handShakeUIState,
        dkmaUiState = onBoardViewModel.dkmaUiState,
        isIssueVisible = onBoardViewModel.isIssueVisible,
        isSolutionVisible = onBoardViewModel.isSolutionVisible,
        alterIssueVisibility = onBoardViewModel::alterIssueVisibility,
        alterSolutionVisibility = onBoardViewModel::alterSolutionVisibility,
        createFirebaseAccount = onBoardViewModel::createFirebaseAccount,
        loadDkmaManufacturer = onBoardViewModel::loadDkmaManufacturer,
        prepareQrCode = onBoardViewModel::prepareQrCode,
        validateReceiver = onBoardViewModel::validateReceiver,
        turnHandShakeUICold = onBoardViewModel::turnHandShakeUICold,
        getQrCode = onBoardViewModel::getQrCode,
        navigateToHome = navigateToHome,
        navigateToPreviousScreen = onBoardViewModel::navigateToPreviousScreen,
        navigateToNextScreen = onBoardViewModel::navigateToNextScreen,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardRoute(
    currentScreen: OnBoardingScreens,
    handShakeUI: HandShakeUI,
    dkmaUiState: DkmaUiState,
    isIssueVisible: Boolean,
    isSolutionVisible: Boolean,
    alterIssueVisibility: () -> Unit,
    alterSolutionVisibility: () -> Unit,
    createFirebaseAccount: () -> Unit,
    loadDkmaManufacturer: () -> Unit,
    prepareQrCode: () -> Unit,
    validateReceiver: (scannedReceiverId: String, context: Context, navigateToHome: () -> Unit) -> Unit,
    turnHandShakeUICold: () -> Unit,
    getQrCode: (handShakeString: String, backgroundColor: Int, foregroundColor: Int) -> ImageBitmap,
    navigateToHome: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    navigateToNextScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            currentScreen.title
                        ),
                        style = if (
                            windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT
                            || windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
                        ) {
                            MaterialTheme.typography.titleLarge
                        } else {
                            MaterialTheme.typography.displayLarge
                        },
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visibleState = MutableTransitionState(
                            currentScreen != OnBoardingScreens.Welcome
                        ),
                        enter = slideInHorizontally(),
                        exit = slideOutHorizontally()
                    ) {
                        IconButton(
                            onClick = navigateToPreviousScreen,
                        ) {
                            Icon(
                                imageVector = ArrowBackIcon,
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                modifier = Modifier
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing,
        floatingActionButton = {
            if (
                currentScreen != OnBoardingScreens.Connection2
            ) {
                ExtendedFloatingActionButton(
                    navigateToNextScreen
                ) {
                    Text(
                        text = if (currentScreen == OnBoardingScreens.Welcome) {
                            stringResource(R.string.agree)
                        } else {
                            stringResource(R.string.proceed)
                        }
                    )
                }
            }
        },
        modifier = modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.safeContent)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->

        val commonModifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(dimensionResource(R.dimen.padding_medium))

        when (currentScreen) {

            OnBoardingScreens.Welcome -> WelcomeScreen(
                commonModifier
            )

            OnBoardingScreens.DkmaScreen -> DkmaScreen(
                createFirebaseAccount,
                dkmaUiState,
                isIssueVisible,
                isSolutionVisible,
                alterIssueVisibility,
                alterSolutionVisibility,
                loadDkmaManufacturer,
                commonModifier
                    .verticalScroll(scrollState)
            )

            OnBoardingScreens.Connection1 -> Connection1Screen(
                getQrCode,
                handShakeUI,
                prepareQrCode,
                turnHandShakeUICold,
                commonModifier
            )

            OnBoardingScreens.Connection2 -> Connection2Screen(
                validateReceiver,
                navigateToHome,
                navigateToPreviousScreen,
                commonModifier
            )
        }
    }
}

@Preview
@Composable
private fun OnBoardPreview() {
    AppTheme {
        AppBackground {
            OnBoardRoute(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}