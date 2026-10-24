package com.wa2c.android.cifsdocumentsprovider.presentation.ui.settings.components

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wa2c.android.cifsdocumentsprovider.presentation.ext.Language
import com.wa2c.android.cifsdocumentsprovider.common.values.OPEN_FILE_LIMIT_DEFAULT
import com.wa2c.android.cifsdocumentsprovider.common.values.OPEN_FILE_LIMIT_MAX
import com.wa2c.android.cifsdocumentsprovider.common.values.OPEN_FILE_LIMIT_MIN
import com.wa2c.android.cifsdocumentsprovider.common.values.UiTheme
import com.wa2c.android.cifsdocumentsprovider.presentation.R
import com.wa2c.android.cifsdocumentsprovider.presentation.ext.getLabel
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.OptionItem
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.SectionCard
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.Theme

/**
 * Split "Title\n(Note)" resources into headline and supporting text.
 */
private fun String.splitTitle(): Pair<String, String?> {
    val lines = split("\n", limit = 2)
    val note = lines.getOrNull(1)?.trim()?.removePrefix("(")?.removeSuffix(")")
    return lines.first() to note
}

/**
 * Settings Screen
 */
@Composable
internal fun SettingsList(
    theme: MutableState<UiTheme>,
    language: MutableState<Language>,
    openFileLimit: MutableState<Int>,
    useForeground: MutableState<Boolean>,
    useAsLocal: MutableState<Boolean>,
    onShowKnownHosts: () -> Unit,
    onShowLibraries: () -> Unit,
    onStartIntent: (Intent) -> Unit,
) {
    val context = LocalContext.current

    // Scrollable container
    LazyColumn(
        contentPadding = PaddingValues(bottom = Theme.Sizes.L),
    ) {
        // General
        item {
            TitleItem(text = stringResource(id = R.string.settings_section_set))
            SectionCard {
                // UI Theme
                SettingsSingleChoiceItem(
                    title = stringResource(id = R.string.settings_set_theme),
                    items = UiTheme.entries.map { OptionItem(it, it.getLabel(context)) },
                    selectedItem = theme,
                    iconRes = R.drawable.ic_palette,
                )
                // Language
                SettingsSingleChoiceItem(
                    title = stringResource(id = R.string.settings_set_language),
                    items = Language.entries.map { OptionItem(it, it.getLabel(context)) },
                    selectedItem = language,
                    iconRes = R.drawable.ic_language,
                )
                // Open File Limit
                val (limitTitle, limitNote) = stringResource(id = R.string.settings_open_file_limit).splitTitle()
                SettingsInputNumberItem(
                    text = limitTitle,
                    supportingText = limitNote,
                    value = openFileLimit,
                    maxValue = OPEN_FILE_LIMIT_MAX,
                    minValue = OPEN_FILE_LIMIT_MIN,
                    iconRes = R.drawable.ic_folder_open,
                )
                // Use Foreground Service
                SettingsCheckItem(
                    text = stringResource(R.string.settings_set_use_foreground),
                    checked = useForeground,
                    iconRes = R.drawable.ic_bell,
                )
                // Use Local
                val (localTitle, localNote) = stringResource(id = R.string.settings_set_use_as_local).splitTitle()
                SettingsCheckItem(
                    text = localTitle,
                    supportingText = localNote,
                    checked = useAsLocal,
                    iconRes = R.drawable.ic_phone,
                )
            }
        }

        // Import / Export
        item {
            TitleItem(text = stringResource(R.string.settings_section_transfer))
            SectionCard {
                SettingsExportItem(
                    text = stringResource(R.string.settings_transfer_export),
                    supportingText = stringResource(R.string.ui_settings_export_desc),
                    iconRes = R.drawable.ic_upload,
                )
                SettingsImportItem(
                    text = stringResource(R.string.settings_transfer_import),
                    supportingText = stringResource(R.string.ui_settings_import_desc),
                    iconRes = R.drawable.ic_download,
                )
            }
        }

        // Information
        item {
            TitleItem(text = stringResource(id = R.string.settings_section_info))
            SectionCard {
                // Known Hosts
                SettingsItem(
                    text = stringResource(id = R.string.settings_info_known_hosts),
                    supportingText = stringResource(id = R.string.ui_settings_known_hosts_desc),
                    iconRes = R.drawable.ic_key,
                ) {
                    onShowKnownHosts()
                }
                // Libraries
                SettingsItem(
                    text = stringResource(id = R.string.settings_info_libraries),
                    supportingText = stringResource(id = R.string.ui_settings_libraries_desc),
                    iconRes = R.drawable.ic_info,
                ) {
                    onShowLibraries()
                }
                // Contributors
                SettingsItem(
                    text = stringResource(id = R.string.settings_info_contributors),
                    supportingText = stringResource(id = R.string.ui_settings_contributors_desc),
                    iconRes = R.drawable.ic_people,
                ) {
                    onStartIntent(
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/wa2c/cifs-documents-provider/graphs/contributors"))
                    )
                }
                // App
                SettingsItem(
                    text = stringResource(id = R.string.settings_info_app),
                    supportingText = stringResource(id = R.string.ui_settings_app_desc),
                    iconRes = R.drawable.ic_settings,
                ) {
                    onStartIntent(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + context.packageName)
                        )
                    )
                }
            }
            Spacer(Modifier.height(Theme.Sizes.M))
        }
    }
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
private fun SettingsListPreview() {
    Theme.AppTheme {
        SettingsList(
            theme = remember { mutableStateOf(UiTheme.DEFAULT) },
            language = remember { mutableStateOf(Language.default) },
            openFileLimit = remember { mutableIntStateOf(OPEN_FILE_LIMIT_DEFAULT) },
            useForeground = remember { mutableStateOf(false) },
            useAsLocal = remember { mutableStateOf(false) },
            onShowKnownHosts = {},
            onShowLibraries = {},
            onStartIntent = {},
        )
    }
}
