package com.wa2c.android.cifsdocumentsprovider.presentation.ui.settings.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.wa2c.android.cifsdocumentsprovider.presentation.R
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.Theme

/**
 * Settings item.
 */
@Composable
internal fun SettingsItem(
    text: String,
    supportingText: String? = null,
    @DrawableRes iconRes: Int? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)?,
) {
    ListItem(
        headlineContent = { Text(text = text) },
        supportingContent = supportingText?.let { { Text(text = it) } },
        leadingContent = iconRes?.let {
            {
                Icon(
                    imageVector = ImageVector.vectorResource(id = it),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },
        trailingContent = trailingContent,
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
    )
}

/**
 * Preview
 */
@Preview(
    name = "Preview",
    group = "Group",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
private fun SettingsItemPreview() {
    Theme.AppTheme {
        SettingsItem(
            text = "Sprache",
            supportingText = "Deutsch",
            iconRes = R.drawable.ic_language,
        ) {}
    }
}
