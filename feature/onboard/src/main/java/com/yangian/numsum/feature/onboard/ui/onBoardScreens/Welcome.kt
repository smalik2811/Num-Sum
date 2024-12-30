package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.yangian.numsum.core.designsystem.MultiDevicePreview
import com.yangian.numsum.core.designsystem.component.AppBackground
import com.yangian.numsum.core.designsystem.component.LauncherIconBox
import com.yangian.numsum.core.designsystem.theme.AppTheme
import com.yangian.numsum.feature.onboard.R

@Composable
fun CompactPortraitWelcomeScreen(
    aboutApp: AnnotatedString,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(5f))

        LauncherIconBox(
            modifier = Modifier
        )

        Spacer(modifier = Modifier.weight(2f))

        Text(
            text = aboutApp,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.weight(4f))

        Text(
            text = appUsageAgreementText(),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Justify,
        )

        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun MediumPortraitWelcomeScreen(
    aboutApp: AnnotatedString,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Spacer(modifier = Modifier.weight(5f))

        LauncherIconBox(
            modifier = Modifier
        )

        Spacer(modifier = Modifier.weight(2f))

        Text(
            text = aboutApp,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.weight(4f))

        Text(
            text = appUsageAgreementText(splitLine = true),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun ExpandedPortraitWelcomeScreen(
    aboutApp: AnnotatedString,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Spacer(modifier = Modifier.weight(5f))

        LauncherIconBox(
            modifier = Modifier
        )

        Spacer(modifier = Modifier.weight(2f))

        Text(
            text = aboutApp,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.weight(4f))

        Text(
            text = appUsageAgreementText(splitLine = true),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun PortraitWelcomeScreen(
    aboutApp: AnnotatedString,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> {
            CompactPortraitWelcomeScreen(
                aboutApp,
                modifier
            )
        }

        WindowWidthSizeClass.MEDIUM -> {
            MediumPortraitWelcomeScreen(
                aboutApp,
                modifier
            )
        }

        WindowWidthSizeClass.EXPANDED -> {
            ExpandedPortraitWelcomeScreen(
                aboutApp,
                modifier
            )
        }
    }
}

@Composable
fun CompactLandscapeWelcomeScreen(
    aboutApp: AnnotatedString,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().weight(3f)
        ) {

            LauncherIconBox(
                maxDimensionFactor = 0.4f,
                modifier = Modifier
            )

            Text(
                text = aboutApp,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.5f)
            )

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ){
            Text(
                text = appUsageAgreementText(splitLine = true),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun MediumLandscapeWelcomeScreen(
    aboutApp: AnnotatedString,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().weight(3f)
        ) {

            LauncherIconBox(
                modifier = Modifier
            )

            Text(
                text = aboutApp,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.5f)
            )

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ){
            Text(
                text = appUsageAgreementText(splitLine = true),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ExpandedLandscapeWelcomeScreen(
    aboutApp: AnnotatedString,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().weight(3f)
        ) {

            LauncherIconBox(
                modifier = Modifier
            )

            Text(
                text = aboutApp,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.5f)
            )

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ){
            Text(
                text = appUsageAgreementText(splitLine = true),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun LandscapeWelcomeScreen(
    aboutApp: AnnotatedString,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    when (windowSizeClass.windowHeightSizeClass) {
        WindowHeightSizeClass.COMPACT -> {
            CompactLandscapeWelcomeScreen(
                aboutApp,
                modifier
            )
        }

        WindowHeightSizeClass.MEDIUM -> {
            MediumLandscapeWelcomeScreen(
                aboutApp,
                modifier
            )
        }

        WindowHeightSizeClass.EXPANDED -> {
            ExpandedLandscapeWelcomeScreen(
                aboutApp,
                modifier
            )
        }
    }
}

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    val screenOrientation = LocalContext.current.resources.configuration.orientation
    val aboutApp: AnnotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(stringResource(R.string.app_name))
        }
        append(stringResource(R.string.space_string))
        append(stringResource(R.string.welcome_description))
    }

    when (screenOrientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LandscapeWelcomeScreen(
                aboutApp,
                modifier
            )
        }

        else -> {
            PortraitWelcomeScreen(
                aboutApp,
                modifier
            )
        }
    }
}

@Composable
private fun appUsageAgreementText(splitLine: Boolean = false): AnnotatedString {
    val privacyPolicyUrl = stringResource(R.string.privacy_policy_document_link)
    val termsOfServiceUrl = stringResource(R.string.terms_of_service_document_link)
    val eulaUrl = stringResource(R.string.eula_document_link)

    val appUsageAgreementText = buildAnnotatedString {
        append("Please review our")
        append(stringResource(R.string.space_string))

        // Privacy Policy
        withLink(
            LinkAnnotation.Url(
                url = privacyPolicyUrl,
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            )
        ) {
            append("Privacy Policy")
        }
        append(stringResource(R.string.space_string))

        // Terms of Services
        withLink(
            LinkAnnotation.Url(
                url = termsOfServiceUrl,
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            )
        ) {
            append("Terms of Services")
        }
        append(", and ")

        // EULA
        withLink(
            LinkAnnotation.Url(
                url = eulaUrl,
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            )
        ) {
            append("EULA")
        }

        append(" before proceeding.${if (splitLine) "\n" else ""}By clicking \"Agree and Proceed,\" you indicate your acceptance of these agreements.")
    }

    return appUsageAgreementText
}

@MultiDevicePreview
@Composable
private fun WelcomeScreenPreviewPhone() {
    AppTheme {
        AppBackground {
            WelcomeScreen(Modifier.fillMaxSize())
        }
    }
}