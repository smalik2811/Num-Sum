package com.yangian.numsum.core.designsystem

import androidx.compose.ui.tooling.preview.Preview

@Preview(group = "Portrait", device = "spec:parent=pixel_9_pro", name = "LargePhonePortrait")
@Preview(group = "Landscape",
    device = "spec:parent=pixel_9_pro,orientation=landscape", name = "LargePhoneLandscape")

@Preview(group = "Portrait", device = "id:Nexus One", name = "SmallPhonePortrait")
@Preview(group = "Landscape",
    device = "spec:parent=Nexus S,orientation=landscape", name = "SmallPhoneLandscape"
)
annotation class PhonePreview


@Preview(
    group = "Portrait",
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait", name = "TabletPortrait")
@Preview(
    group = "Landscape",
    device = "spec:width=1280dp,height=800dp,dpi=240", name = "TabletLandscape"
)
annotation class TabletPreview


@Preview(
    group = "Portrait",
    device = "spec:width=1920dp,height=1080dp,dpi=160,orientation=portrait", name = "DesktopPortrait"
)
@Preview(group = "Landscape",
    device = "spec:width=1920dp,height=1080dp,dpi=160", name = "DesktopLandscape"
)
annotation class DesktopPreview


@PhonePreview
@TabletPreview
@DesktopPreview
annotation class MultiDevicePreview