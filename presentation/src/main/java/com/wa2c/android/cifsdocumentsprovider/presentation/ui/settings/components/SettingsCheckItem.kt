package com.wa2c.android.cifsdocumentsprovider.presentation.ui.settings.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.Theme

/**
 * Settings check item (switch).
 */
@Composable
internal fun SettingsCheckItem(
    text: String,
    checked: MutableState<Boolean>,
    supportingText: String? = null,
    @DrawableRes iconRes: Int? = null,
) {
    SettingsItem(
        text = text,
        supportingText = supportingText,
        iconRes = iconRes,
        trailingContent = {
            Switch(
                checked = checked.value,
                onCheckedChange = { checked.value = it },
            )
        },
        onClick = { checked.value = !checked.value },
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
private fun SettingsCheckItemPreview() {
    Theme.AppTheme {
        SettingsCheckItem(
            text = "Benachrichtigung anzeigen",
            checked = remember { mutableStateOf(true) },
        )
    }
}
