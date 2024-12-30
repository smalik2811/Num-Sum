package com.yangian.numsum.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.yangian.numsum.core.designsystem.icon.BackspaceIcon
import com.yangian.numsum.core.designsystem.theme.AppTheme

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun CalculatorIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier
                .fillMaxSize(0.32f)
                .align(Alignment.Center)
        )
    }
}

@Preview(
    showSystemUi = true, device = "id:pixel_8_pro",
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun CalculatorButtonPreview() {
    AppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CalculatorButton(
                    text = "1",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(ratio = 1f, true),
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {},
                )
                CalculatorButton(
                    text = "2",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(ratio = 1f, true),
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {},
                )
                CalculatorButton(
                    text = "3",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(ratio = 1f, true),
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {},
                )
                CalculatorButton(
                    text = "+",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(ratio = 1f, true),
                    backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {},
                )

            }
        }
    }
}

@Preview(
    showSystemUi = true, device = "id:pixel_8_pro",
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, apiLevel = 34
)
@Composable
private fun CalculatorIconButtonPreview() {
    AppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CalculatorIconButton(
                    icon = BackspaceIcon,
                    contentDescription = "",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(ratio = 1f, true),
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {},
                )

                CalculatorIconButton(
                    icon = BackspaceIcon,
                    contentDescription = "",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(ratio = 1f, true),
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {},
                )

                CalculatorIconButton(
                    icon = BackspaceIcon,
                    contentDescription = "",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(ratio = 1f, true),
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {},
                )

                CalculatorIconButton(
                    icon = BackspaceIcon,
                    contentDescription = "",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(ratio = 1f, true),
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {},
                )
            }
        }
    }
}