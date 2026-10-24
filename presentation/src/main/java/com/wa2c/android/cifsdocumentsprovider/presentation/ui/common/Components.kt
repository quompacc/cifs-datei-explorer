package com.wa2c.android.cifsdocumentsprovider.presentation.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wa2c.android.cifsdocumentsprovider.common.values.ProtocolType
import com.wa2c.android.cifsdocumentsprovider.common.values.StorageType
import com.wa2c.android.cifsdocumentsprovider.presentation.R

/**
 * Section header shown above a [SectionCard].
 */
@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(
            start = Theme.Sizes.M + Theme.Sizes.SS,
            end = Theme.Sizes.M,
            top = Theme.Sizes.L,
            bottom = Theme.Sizes.S,
        )
    )
}

/**
 * Rounded container grouping related items.
 */
@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.Sizes.M),
    ) {
        Column(content = content)
    }
}

/**
 * Icon on a tonal rounded tile.
 */
@Composable
fun IconTile(
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.3f))
            .background(containerColor),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = iconRes),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(size * 0.55f),
        )
    }
}

/**
 * Small pill label.
 */
@Composable
fun PillLabel(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}

/** Short protocol label */
val StorageType.shortLabel: String
    get() = when (this) {
        StorageType.JCIFS, StorageType.SMBJ -> "SMB 2/3"
        StorageType.JCIFS_LEGACY -> "SMB 1"
        StorageType.APACHE_FTP -> "FTP"
        StorageType.APACHE_FTPS -> "FTPS"
        StorageType.APACHE_SFTP -> "SFTP"
    }

/** Protocol icon */
val StorageType.iconRes: Int
    @DrawableRes get() = when (protocol) {
        ProtocolType.SMB -> R.drawable.ic_dns
        ProtocolType.SFTP -> R.drawable.ic_lock
        ProtocolType.FTP, ProtocolType.FTPS -> R.drawable.ic_cloud
    }
