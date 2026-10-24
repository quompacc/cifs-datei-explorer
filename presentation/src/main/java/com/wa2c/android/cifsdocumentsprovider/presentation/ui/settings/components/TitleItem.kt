package com.wa2c.android.cifsdocumentsprovider.presentation.ui.settings.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.SectionHeader
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.Theme

/**
 * Title item.
 */
@Composable
internal fun TitleItem(
    text: String,
) {
    SectionHeader(text = text)
}

@Preview(
    name = "Preview",
    group = "Group",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
private fun TitleItemPreview() {
    Theme.AppTheme {
        TitleItem(
            text = "Title Item",
        )
    }
}
