package com.yangian.numsum.core.ui

import android.content.res.Configuration
import android.telephony.PhoneNumberUtils
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.yangian.numsum.core.designsystem.icon.CallMadeIcon
import com.yangian.numsum.core.designsystem.icon.CallMissedIcon
import com.yangian.numsum.core.designsystem.icon.CallReceivedIcon
import com.yangian.numsum.core.designsystem.icon.PhoneDisabledIcon
import com.yangian.numsum.core.designsystem.icon.PhoneMissedIcon
import com.yangian.numsum.core.designsystem.icon.VoicemailIcon
import com.yangian.numsum.core.designsystem.icon.WifiCallingIcon
import com.yangian.numsum.core.designsystem.theme.AppTheme
import com.yangian.numsum.core.designsystem.theme.extendedDark
import com.yangian.numsum.core.designsystem.theme.extendedLight
import com.yangian.numsum.core.model.CallResource
import com.yangian.numsum.core.model.getDateTimeString
import com.yangian.numsum.core.model.getDurationString
import java.util.Locale

@Composable
fun CallResourceListItem(
    callResource: CallResource,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                if (callResource.name.isNullOrEmpty()) {
                    Text(
                        text = "Unknown Number",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                } else {
                    Text(
                        text = "${callResource.name}",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                when (callResource.type) {
                    1 -> Icon(
                        imageVector = CallReceivedIcon,
                        contentDescription = "Call received",
                        tint = when (isSystemInDarkTheme()) {
                            true -> extendedDark.success.color
                            false -> extendedLight.success.color
                        },
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
            }
        },
        supportingContent = {
            Column {
                Text(
                    text = PhoneNumberUtils.formatNumber(
                        callResource.number,
                        Locale.getDefault().country
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = callResource.getDateTimeString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        leadingContent = {
            Checkbox(checked = false, onCheckedChange = {})
        },
        trailingContent = {
            Text(
                text = callResource.getDurationString(),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        },
    )
}

@Preview(
    device = "id:pixel_8_pro",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    apiLevel = 33,
    locale = "en-in"
)

@Composable
private fun CallResourceListItemPreview() {
    AppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
                for (i in 1..2) {
                    CallResourceListItem(
                        callResource = CallResource(
                            id = 5,
                            name = "Richard Khijkjoslksjljlkjs",
                            number = "+91${(1000000000..9999999999).random()}",
                            timestamp = (1546300800000L..1717027200000L).random(),
                            duration = (0L..86400L).random(),
                            type = (1..7).random()
                        )
                    )
                }
            }
        }
    }
}