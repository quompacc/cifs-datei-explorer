package com.wa2c.android.cifsdocumentsprovider.presentation.ui.edit.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wa2c.android.cifsdocumentsprovider.presentation.R
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.Theme

@Composable
fun UriText(
    title: String? = null,
    uriText: String,
    onCopyToClipboard: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = uriText.isNotEmpty()) { onCopyToClipboard(uriText) }
            .padding(start = Theme.Sizes.M + Theme.Sizes.SS, end = Theme.Sizes.S, top = 6.dp, bottom = 6.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = uriText.ifEmpty { "–" },
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
            )
        }
        if (uriText.isNotEmpty()) {
            IconButton(onClick = { onCopyToClipboard(uriText) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_copy),
                    contentDescription = stringResource(id = R.string.ui_edit_copy),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview(
    name = "Preview",
    group = "Group",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
private fun UriTextPreview() {
    Theme.AppTheme {
        UriText(
            title = "Speicher-URI",
            uriText = "smb://nas/daten/",
            onCopyToClipboard = {},
        )
    }
}
