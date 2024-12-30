package com.yangian.numsum.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yangian.numsum.core.designsystem.MultiDevicePreview
import com.yangian.numsum.core.designsystem.R
import com.yangian.numsum.core.designsystem.theme.AppTheme
import kotlin.math.min

@Composable
fun LauncherIconBox(
    minDimensionFactor: Float = 0.1f,
    maxDimensionFactor: Float = 0.3f,
    @DrawableRes foregroundResource: Int = R.mipmap.app_launcher_foreground,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .wrapContentSize()
            .shadow(
                elevation = dimensionResource(R.dimen.elevation_level_2),
                shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_extra_large))
            )
    ) {
        val minDimension = (min(maxWidth.value, maxHeight.value) * minDimensionFactor).dp
        val maxDimension = (min(maxWidth.value, maxHeight.value) * maxDimensionFactor).dp
        Image(
            painter = painterResource(R.mipmap.launcher_background),
            contentDescription = stringResource(R.string.app_logo),
            modifier = Modifier
                .heightIn(minDimension, maxDimension)
                .widthIn(minDimension, maxDimension)
                .aspectRatio(1f, false)
                .clip(
                    RoundedCornerShape(
                        dimensionResource(R.dimen.corner_radius_extra_large)
                    )
                )
        )

        Image(
            painter = painterResource(foregroundResource),
            contentDescription = stringResource(R.string.app_logo),
            modifier = Modifier
                .heightIn(minDimension, maxDimension)
                .widthIn(minDimension, maxDimension)
                .aspectRatio(1f, false)
                .clip(
                    RoundedCornerShape(
                        dimensionResource(R.dimen.corner_radius_extra_large)
                    )
                )
        )
    }
}

@Composable
fun LauncherIcon(
    @DrawableRes foregroundResource: Int = R.mipmap.app_launcher_foreground,
    modifier: Modifier = Modifier
) {
    Box {
        Image(
            painter = painterResource(R.mipmap.launcher_background),
            contentDescription = stringResource(R.string.app_logo),
            modifier = modifier
                .clip(
                    RoundedCornerShape(
                        dimensionResource(R.dimen.corner_radius_extra_large)
                    )
                )
        )

        Image(
            painter = painterResource(foregroundResource),
            contentDescription = stringResource(R.string.app_logo),
            modifier = modifier
                .clip(
                    RoundedCornerShape(
                        dimensionResource(R.dimen.corner_radius_extra_large)
                    )
                )
        )
    }
}

@MultiDevicePreview
@Composable
private fun LauncherIconBoxPreview(modifier: Modifier = Modifier) {
    AppTheme {
        AppBackground(
            modifier = modifier
                .fillMaxSize()
        ) {
            LauncherIconBox(
                foregroundResource = R.mipmap.foreign_app_launcher_foreground,
                modifier = modifier
//                    .padding(40.dp)
            )
        }
    }
}