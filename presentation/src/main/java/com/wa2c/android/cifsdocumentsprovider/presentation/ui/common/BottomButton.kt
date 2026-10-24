package com.wa2c.android.cifsdocumentsprovider.presentation.ui.common

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun BottomButton(label: String, subText: String? = null, onClick: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = Theme.Sizes.M, vertical = 12.dp)
        ) {
            subText?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(bottom = Theme.Sizes.S, start = Theme.Sizes.SS, end = Theme.Sizes.SS)
                        .horizontalScroll(ScrollState(Int.MAX_VALUE))
                )
            }

            Button(
                onClick = onClick,
                contentPadding = PaddingValues(vertical = 14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = label)
            }
        }
    }
}
