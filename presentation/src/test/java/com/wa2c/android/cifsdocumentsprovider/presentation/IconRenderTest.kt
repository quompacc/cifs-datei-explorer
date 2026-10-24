package com.wa2c.android.cifsdocumentsprovider.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.resources.Density
import org.junit.Rule
import org.junit.Test

/**
 * Renders the launcher icon.
 * - icon_store: 512x512 PNG for fastlane/metadata/android/en-US/images/icon.png
 * - icon_preview: icon in different launcher masks, themed and notification variant
 */
class IconRenderTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig(
            screenWidth = 512,
            screenHeight = 512,
            density = Density.MEDIUM,
            xdpi = 160,
            ydpi = 160,
        ),
        theme = "android:Theme.Material.NoActionBar",
        maxPercentDifference = 0.1,
    )

    @Composable
    private fun AppIcon(size: Dp, shape: Shape) {
        // Launchers show the inner 72 of 108 units of an adaptive icon
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size).clip(shape)) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.bg_launcher),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.requiredSize(size * 1.5f),
            )
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.requiredSize(size * 1.5f),
            )
        }
    }

    @Test
    fun icon_store() = paparazzi.snapshot {
        Box(Modifier.fillMaxSize().background(Color.Transparent)) {
            AppIcon(size = 512.dp, shape = RoundedCornerShape(0))
        }
    }

    @Test
    fun icon_preview() = paparazzi.snapshot {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().background(Color(0xFFE8EAF0)).padding(16.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                AppIcon(size = 140.dp, shape = CircleShape)
                AppIcon(size = 140.dp, shape = RoundedCornerShape(30))
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Themed icon (Android 13+)
                Box(
                    modifier = Modifier.size(140.dp).clip(CircleShape).background(Color(0xFFD3E3FD)),
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_monochrome),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color(0xFF0B3C8C)),
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.requiredSize(210.dp).align(Alignment.Center),
                    )
                }
                // Notification icon
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(140.dp).clip(RoundedCornerShape(20)).background(Color(0xFF1F1F1F)),
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_notification),
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                    )
                }
                AppIcon(size = 48.dp, shape = CircleShape)
            }
        }
    }
}
