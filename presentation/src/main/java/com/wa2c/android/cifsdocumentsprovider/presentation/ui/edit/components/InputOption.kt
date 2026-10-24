import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
 * Input Option
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> InputOption(
    title: String,
    items: List<OptionItem<T>>,
    value: T,
    focusManager: FocusManager,
    enabled: Boolean = true,
    onChange: (value: T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.Sizes.M, vertical = 6.dp),
    ) {
        OutlinedTextField(
            value = items.firstOrNull { it.value == value }?.label ?: "",
            label = { Text(title) },
            enabled = enabled,
            readOnly = true,
            singleLine = true,
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = MaterialTheme.shapes.small,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus()
            },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.label) },
                    enabled = enabled,
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    onClick = {
                        onChange(item.value)
                        expanded = false
                        focusManager.clearFocus()
                    }
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
private fun InputOptionPreview() {
    Theme.AppTheme {
        InputOption(
            title = "Protokoll",
            items = listOf(OptionItem(1, "SMB2,3 (SMBJ)"), OptionItem(2, "SMB1")),
            value = 1,
            focusManager = LocalFocusManager.current,
        ) {}
    }
}
