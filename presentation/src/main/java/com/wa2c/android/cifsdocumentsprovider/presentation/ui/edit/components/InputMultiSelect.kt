import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.OptionItem
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.Theme

/**
 * Input Multi Select (filter chips)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> InputMultiSelect(
    title: String,
    items: List<OptionItem<T>>,
    emptyLabel: String,
    values: Set<T>,
    focusManager: FocusManager,
    enabled: Boolean = true,
    onChange: (value: Set<T>) -> Unit,
) {
    var checkedValues by remember { mutableStateOf(values) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.Sizes.M + Theme.Sizes.SS, vertical = Theme.Sizes.S)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = items.filter { checkedValues.contains(it.value) }.let { list ->
                if (list.isEmpty()) emptyLabel else list.joinToString(", ") { it.label }
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Theme.Sizes.S),
            modifier = Modifier.padding(top = Theme.Sizes.SS),
        ) {
            items.forEach { item ->
                val selected = checkedValues.contains(item.value)
                FilterChip(
                    selected = selected,
                    enabled = enabled,
                    onClick = {
                        checkedValues = checkedValues.toMutableSet().apply {
                            if (contains(item.value)) remove(item.value) else add(item.value)
                        }
                        onChange(checkedValues)
                    },
                    label = { Text(item.label) },
                    leadingIcon = if (selected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    },
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
private fun InputMultiSelectPreview() {
    Theme.AppTheme {
        InputMultiSelect(
            title = "Vorschaubilder",
            items = listOf(OptionItem(1, "Bilder"), OptionItem(2, "Audio"), OptionItem(3, "Videos")),
            emptyLabel = "Deaktiviert",
            values = setOf(1),
            focusManager = LocalFocusManager.current,
        ) {}
    }
}
