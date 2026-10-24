package com.wa2c.android.cifsdocumentsprovider.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.resources.NightMode
import com.wa2c.android.cifsdocumentsprovider.common.values.OPEN_FILE_LIMIT_DEFAULT
import com.wa2c.android.cifsdocumentsprovider.common.values.StorageType
import com.wa2c.android.cifsdocumentsprovider.common.values.UiTheme
import com.wa2c.android.cifsdocumentsprovider.domain.model.HostData
import com.wa2c.android.cifsdocumentsprovider.domain.model.RemoteConnection
import com.wa2c.android.cifsdocumentsprovider.domain.model.RemoteConnectionIndex
import com.wa2c.android.cifsdocumentsprovider.presentation.ext.Language
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.Theme
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.edit.EditScreenContainer
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.home.HomeScreenContainer
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.host.HostScreenContainer
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.settings.SettingsScreenContainer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Renders the main screens (German locale) to PNG files.
 * Run: ./gradlew :presentation:recordPaparazziDebug
 * Output: presentation/src/test/snapshots/images/
 */
@RunWith(Parameterized::class)
class ScreenshotTest(private val dark: Boolean) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "dark={0}")
        fun params() = listOf(false, true)
    }

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6.copy(
            locale = "de",
            nightMode = if (dark) NightMode.NIGHT else NightMode.NOTNIGHT,
        ),
        theme = "android:Theme.Material.NoActionBar",
        maxPercentDifference = 0.1,
    )

    private fun snap(name: String, content: @Composable () -> Unit) {
        paparazzi.snapshot(name = name + if (dark) "_dark" else "_light") {
            Theme.AppTheme(darkTheme = dark, dynamicColor = false) {
                content()
            }
        }
    }

    private val connections = listOf(
        RemoteConnectionIndex(id = "1", name = "NAS", storage = StorageType.SMBJ, uri = "smb://192.168.0.10/daten/"),
        RemoteConnectionIndex(id = "2", name = "Büro-PC", storage = StorageType.JCIFS, uri = "smb://buero-pc/freigabe/"),
        RemoteConnectionIndex(id = "3", name = "Fotos", storage = StorageType.SMBJ, uri = "smb://nas/fotos/2026/"),
    )

    @Test
    fun home() = snap("home") {
        HomeScreenContainer(
            snackbarHostState = SnackbarHostState(),
            connectionList = connections,
            onClickMenuOpenFile = {},
            onClickMenuSettings = {},
            onClickItem = {},
            onClickAddItem = {},
            onClickSearchItem = {},
            onDragAndDrop = { _, _ -> },
        )
    }

    @Test
    fun homeEmpty() = snap("home_empty") {
        HomeScreenContainer(
            snackbarHostState = SnackbarHostState(),
            connectionList = emptyList(),
            onClickMenuOpenFile = {},
            onClickMenuSettings = {},
            onClickItem = {},
            onClickAddItem = {},
            onClickSearchItem = {},
            onDragAndDrop = { _, _ -> },
        )
    }

    @Test
    fun edit() = snap("edit") {
        EditScreenContainer(
            snackbarHostState = SnackbarHostState(),
            isNew = true,
            isBusy = false,
            connectionState = mutableStateOf(
                RemoteConnection(id = "nas", name = "NAS", host = "192.168.0.10", storage = StorageType.SMBJ, user = "eduard", password = "geheim", folder = "daten")
            ),
            connectionResult = null,
            onClickBack = {},
            onClickDelete = {},
            onClickSearchHost = {},
            onClickSelectFolder = {},
            onClickImportKey = {},
            onClickCheckConnection = {},
            onCopyToClipboard = {},
            onClickSave = {},
        )
    }

    @Test
    fun settings() = snap("settings") {
        SettingsScreenContainer(
            snackbarHostState = SnackbarHostState(),
            theme = mutableStateOf(UiTheme.DEFAULT),
            language = mutableStateOf(Language.GERMAN),
            openFileLimit = mutableIntStateOf(OPEN_FILE_LIMIT_DEFAULT),
            useForeground = mutableStateOf(true),
            useAsLocal = mutableStateOf(false),
            knownHosts = mutableStateOf(emptyList()),
            onCopyToClipboard = {},
            onDeleteKnownHost = {},
            onTransitEdit = {},
            onStartIntent = {},
            onClickBack = {},
        )
    }

    @Test
    fun host() = snap("host") {
        HostScreenContainer(
            snackbarHostState = SnackbarHostState(),
            isInit = true,
            isLoading = true,
            hostList = listOf(
                HostData(hostName = "nas.fritz.box", ipAddress = "192.168.178.20", detectionTime = 0),
                HostData(hostName = "DESKTOP-4711", ipAddress = "192.168.178.31", detectionTime = 0),
                HostData(hostName = "192.168.178.40", ipAddress = "192.168.178.40", detectionTime = 0),
            ),
            onClickBack = {},
            onClickSort = {},
            onClickReload = {},
            onClickItem = {},
            onClickSet = {},
        )
    }
}
