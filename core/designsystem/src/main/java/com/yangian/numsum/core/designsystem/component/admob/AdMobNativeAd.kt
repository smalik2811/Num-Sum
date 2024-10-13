package com.yangian.numsum.core.designsystem.component.admob

import android.content.Context
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT
import com.google.android.gms.ads.nativead.NativeAdView
import com.yangian.numsum.core.designsystem.R
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme

@Composable
private fun LoadAdContent(
    nativeAd: NativeAd?,
    composeView: View,
    modifier: Modifier = Modifier,
) {
    nativeAd?.let { ad ->

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    MaterialTheme.shapes.large
                )
        ) {
            if (ad.images.size > 0) {

                Image(
                    painter = rememberAsyncImagePainter(model = ad.images[0].drawable),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                )
            }

            Row {
                ad.icon?.let { icon ->
                    Image(
                        painter = rememberAsyncImagePainter(model = icon.drawable),
                        contentDescription = ad.advertiser,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .aspectRatio(1f, false)
                    )

                    Spacer(
                        modifier = Modifier.fillMaxWidth(0.04f)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(
                        text = "${ad.headline}",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ad_badge),
                            contentDescription = "Ad badge",
                            modifier = Modifier
                                .size(24.dp)
                        )

                        if (ad.starRating != null) {
                            StarRating(
                                rating = ad.starRating!!,
                                modifier = Modifier.wrapContentSize()
                            )
                        } else {
                            Text(
                                text = ad.store ?: ad.advertiser ?: "",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleSmall,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            ad.body?.let { body ->
                Text(
                    text = body,
                    maxLines = 2,
                )
            }

            Button(
                onClick = {
                    composeView.performClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "${ad.callToAction}",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    } ?: run {
        // Placeholder for loading state or error state
        Text("Loading ad...")
    }
}

@Composable
fun NativeAdView(
    ad: NativeAd,
    modifier: Modifier = Modifier,
    adContent: @Composable (ad: NativeAd, contentView: View) -> Unit,
) {
    val contentViewId by remember { mutableIntStateOf(View.generateViewId()) }
    val adViewId by remember { mutableIntStateOf(View.generateViewId()) }
    AndroidView(
        factory = { context ->
            val contentView = ComposeView(context).apply {
                id = contentViewId
            }
            NativeAdView(context).apply {
                id = adViewId
                addView(contentView)
            }
        },
        update = { view ->
            val adView = view.findViewById<NativeAdView>(adViewId)
            val contentView = view.findViewById<ComposeView>(contentViewId)

            adView.setNativeAd(ad)
            adView.callToActionView = contentView
            contentView.setContent { adContent(ad, contentView) }
        },
        modifier = modifier
    )
}

@Composable
fun CallNativeAd(
    nativeAd: NativeAd,
    modifier: Modifier = Modifier
) {
    NativeAdView(ad = nativeAd, modifier = modifier) { ad, view ->
        LoadAdContent(
            ad,
            view,
            modifier,
        )
    }
}

fun loadNativeAd(context: Context, adUnitId: String, callback: (NativeAd?) -> Unit) {
    val builder = AdLoader.Builder(context, adUnitId)
        .forNativeAd { nativeAd ->
            callback(nativeAd)
        }

    val adLoader = builder
        .withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                callback(null)
            }
        })
        .withNativeAdOptions(
            NativeAdOptions
                .Builder()
                .setRequestCustomMuteThisAd(false)
                .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                .build()
        )
        .build()

    adLoader.loadAd(AdRequest.Builder().build())
}

@Composable
fun StarRating(
    rating: Double,
    maxRating: Int = 5,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
    ) {
        for (i in 1..maxRating) {
            Icon(
                imageVector = when {
                    i < rating.toInt() -> Icons.Filled.StarRate
                    rating.toInt() == i && i < rating -> Icons.AutoMirrored.Filled.StarHalf
                    else -> Icons.Outlined.StarOutline
                },
                contentDescription = "Star $i",
                tint = if (i <= rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(device = "id:Nexus S")
@Composable
private fun StarRatingPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
            StarRating(3.2, 5)
        }
    }
}