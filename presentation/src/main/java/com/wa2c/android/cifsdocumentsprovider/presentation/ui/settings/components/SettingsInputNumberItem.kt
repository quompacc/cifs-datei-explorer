package com.wa2c.android.cifsdocumentsprovider.presentation.ui.settings.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.Theme

/**
 * Settings input number item.
 */
@Composable
internal fun SettingsInputNumberItem(
    text: String,
    value: MutableState<Int>,
    maxValue: Int = 999,
    minValue: Int = 1,
    supportingText: String? = null,
    @DrawableRes iconRes: Int? = null,
) {
    SettingsItem(
        text = text,
        supportingText = supportingText,
        iconRes = iconRes,
        trailingContent = {
            OutlinedTextField(
                value = value.value.toString(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                modifier = Modifier.width(76.dp),
                onValueChange = {
                    val number = it.toIntOrNull() ?: minValue
                    value.value = number.coerceIn(minValue, maxValue)
                }
            )
        },
        onClick = null,
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
private fun SettingsInputNumberItemPreview() {
    Theme.AppTheme {
        SettingsInputNumberItem(
            text = "Max. geöffnete Dateien",
            value = remember { mutableIntStateOf(5) },
        )
    }
}
