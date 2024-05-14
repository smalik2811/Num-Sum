package com.yangian.numsum.core.ui

import android.content.res.Configuration
import android.icu.util.Calendar
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.yangian.numsum.core.designsystem.icon.CallMadeIcon
import com.yangian.numsum.core.designsystem.icon.CallMissedIcon
import com.yangian.numsum.core.designsystem.icon.CallReceivedIcon
import com.yangian.numsum.core.designsystem.icon.PhoneDisabledIcon
import com.yangian.numsum.core.designsystem.icon.PhoneMissedIcon
import com.yangian.numsum.core.designsystem.icon.VoicemailIcon
import com.yangian.numsum.core.designsystem.icon.WifiCallingIcon
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.core.designsystem.theme.extendedDark
import com.yangian.numsum.core.designsystem.theme.extendedLight
import com.yangian.numsum.core.model.CallResource
import com.yangian.numsum.core.model.getDateString
import com.yangian.numsum.core.model.getDurationString

@Composable
fun CallResourceListItem(
    callResource: CallResource,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            if (callResource.name.isNullOrEmpty()) {
                Text(
                    text = "Unknown Number",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            } else {
                Text(
                    text = "${callResource.name}",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        },
        supportingContent = {
            Column {
                Text(
                    text = callResource.number,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = callResource.getDateString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        leadingContent = {
            when (callResource.type) {
                1 -> Icon(
                    imageVector = CallReceivedIcon,
                    contentDescription = "Call received",
                    tint = when (isSystemInDarkTheme()) {
                        true -> extendedDark.success.color
                        false -> extendedLight.success.color
                    }
                )

                2 -> Icon(
                    imageVector = CallMadeIcon,
                    contentDescription = "Call made",
                    tint = when (isSystemInDarkTheme()) {
                        true -> extendedDark.success.color
                        false -> extendedLight.success.color
                    }
                )

                3 -> Icon(
                    imageVector = CallMissedIcon,
                    contentDescription = "Call missed",
                    tint = MaterialTheme.colorScheme.error,
                )

                4 -> Icon(
                    imageVector = VoicemailIcon,
                    contentDescription = "Voicemail",
                    tint = when (isSystemInDarkTheme()) {
                        true -> extendedDark.success.color
                        false -> extendedLight.success.color
                    }
                )

                5 -> Icon(
                    imageVector = PhoneMissedIcon,
                    contentDescription = "Call rejected",
                    tint = MaterialTheme.colorScheme.error,
                )

                6 -> Icon(
                    imageVector = PhoneDisabledIcon,
                    contentDescription = "Call blocked",
                    tint = MaterialTheme.colorScheme.error,
                )

                7 -> Icon(
                    imageVector = WifiCallingIcon,
                    contentDescription = "Answered on another device",
                    tint = when (isSystemInDarkTheme()) {
                        true -> extendedDark.success.color
                        false -> extendedLight.success.color
                    }
                )
            }
        },
        trailingContent = {
            Text(
                text = callResource.getDurationString(),
            )
        }
    )
}

@Preview(
    device = "id:pixel_8_pro", showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE, apiLevel = 33
)
@Composable
private fun CallResourceListItemPreview() {
    NumSumAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
                for (i in 1..4) {
                    CallResourceListItem(
                        callResource = CallResource(
                            id = 5,
                            name = "Richard K.",
                            number = "+91 987-654-3210",
                            timestamp = Calendar.getInstance().timeInMillis,
                            duration = 534,
                            type = 1
                        )
                    )
                }
            }
        }
    }
}