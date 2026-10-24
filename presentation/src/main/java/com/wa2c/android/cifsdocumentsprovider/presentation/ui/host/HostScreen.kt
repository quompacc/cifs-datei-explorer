package com.wa2c.android.cifsdocumentsprovider.presentation.ui.host

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.IconTile
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wa2c.android.cifsdocumentsprovider.common.values.HostSortType
import com.wa2c.android.cifsdocumentsprovider.domain.model.HostData
import com.wa2c.android.cifsdocumentsprovider.presentation.R
import com.wa2c.android.cifsdocumentsprovider.presentation.ext.collectIn
import com.wa2c.android.cifsdocumentsprovider.presentation.ext.labelRes
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.AppSnackbarHost
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.BottomButton
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.CommonDialog
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.DialogButton
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.LoadingIconButton
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.SingleChoiceDialog
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.Theme
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.getAppTopAppBarColors
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.showError

/**
 * Host Screen
 */
@Composable
fun HostScreen(
    viewModel: HostViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onNavigateBack: () -> Unit,
    onSelectItem: (host: String) -> Unit,
    onSetManually: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val showSortDialog = remember { mutableStateOf(false) }
    val selectedHost = remember { mutableStateOf<HostData?>(null) }
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val connectionList = viewModel.hostDataList.collectAsStateWithLifecycle()

    HostScreenContainer(
        snackbarHostState = snackbarHostState,
        isInit = viewModel.isInit,
        isLoading = isLoading.value,
        hostList = connectionList.value,
        onClickBack = { onNavigateBack() },
        onClickSort = { showSortDialog.value = true },
        onClickReload = { viewModel.discovery() },
        onClickItem = { host -> selectedHost.value = host },
        onClickSet = { onSetManually() },
    )

    // Sort dialog
    if (showSortDialog.value) {
        val types =  HostSortType.entries
        SingleChoiceDialog(
            items = types.map { stringResource(id = it.labelRes) },
            selectedIndex = types.indexOfFirst { it == viewModel.sortType.value },
            dismissButton = DialogButton(
                label = stringResource(id = R.string.general_close),
                onClick = { showSortDialog.value = false }
            ),
            onDismiss = { showSortDialog.value = false },
            result = { index, _ ->
                types.getOrNull(index)?.let { viewModel.sort(it) }
                showSortDialog.value = false
            },
        )
    }

    // Confirmation dialog
    selectedHost.value?.let { host ->
        if (host.hostName != host.ipAddress) {
            CommonDialog(
                confirmButtons = listOf(
                    DialogButton(label = stringResource(id = R.string.host_select_host_name)) {
                        onSelectItem(host.hostName)
                        selectedHost.value = null
                    },
                    DialogButton(label = stringResource(id = R.string.host_select_ip_address)) {
                        onSelectItem(host.ipAddress)
                        selectedHost.value = null
                    }
                ),
                dismissButton = DialogButton(label = stringResource(id = R.string.general_close)) {
                    selectedHost.value = null
                },
                onDismiss = {
                    selectedHost.value = null
                }
            ) {
                Text(stringResource(id = R.string.host_select_confirmation_message))
            }
        } else {
            onSelectItem(host.hostName)
            selectedHost.value = null
        }
    }

    LaunchedEffect(Unit) {
        viewModel.result.collectIn(lifecycleOwner) {
            scope.showError(snackbarHostState, it.exceptionOrNull())
        }
    }
}

/**
 * Host Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostScreenContainer(
    snackbarHostState: SnackbarHostState,
    isInit: Boolean,
    isLoading: Boolean,
    hostList: List<HostData>,
    onClickBack: () -> Unit,
    onClickSort: () -> Unit,
    onClickReload: () -> Unit,
    onClickItem: (HostData) -> Unit,
    onClickSet: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.host_title)) },
                colors = getAppTopAppBarColors(),
                actions = {
                    IconButton(
                        onClick = { onClickSort() }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_sort),
                            contentDescription = stringResource(id = R.string.host_sort_button),
                        )
                    }
                    LoadingIconButton(
                        contentDescription = stringResource(id = R.string.host_reload_button),
                        isLoading = isLoading,
                        onClick = onClickReload,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                            contentDescription = stringResource(id = R.string.general_back),
                        )
                    }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            HostList(
                hostList = hostList,
                isLoading = isLoading,
                onClickItem = onClickItem,
            )

            if (isInit) {
                BottomButton(
                    label = stringResource(id = R.string.host_set_manually),
                    onClick = onClickSet,
                )
            }
        }
    }
}

/**
 * Host Screen
 */
@Composable
fun ColumnScope.HostList(
    hostList: List<HostData>,
    isLoading: Boolean = false,
    onClickItem: (HostData) -> Unit,
) {
    if (hostList.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .fillMaxWidth()
                .padding(Theme.Sizes.LL),
        ) {
            IconTile(
                iconRes = R.drawable.ic_lan,
                size = 88.dp,
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(id = if (isLoading) R.string.ui_host_searching else R.string.ui_host_empty),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = Theme.Sizes.M),
            )
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(Theme.Sizes.M),
        verticalArrangement = Arrangement.spacedBy(Theme.Sizes.S),
        modifier = Modifier
            .weight(weight = 1f, fill = true)
    ) {
        items(items = hostList) { hostData ->
            HostItem(
                hostData = hostData,
                onClick = { onClickItem(hostData) },
            )
        }
    }
}

@Composable
private fun HostItem(
    hostData: HostData,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = hostData.hostName,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        supportingContent = if (hostData.hostName != hostData.ipAddress) {
            { Text(text = hostData.ipAddress) }
        } else {
            null
        },
        leadingContent = {
            IconTile(
                iconRes = R.drawable.ic_host,
                size = 44.dp,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        trailingContent = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_right),
                contentDescription = null,
            )
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(enabled = true, onClick = onClick),
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
private fun FolderScreenPreview() {
    Theme.AppTheme {
        HostScreenContainer(
            snackbarHostState = SnackbarHostState(),
            isLoading = false,
            hostList = listOf(
                HostData(
                    hostName = "Host1",
                    ipAddress = "192.168.0.1",
                    detectionTime = 0,
                ),
                HostData(
                    hostName = "Host2",
                    ipAddress = "192.168.0.2",
                    detectionTime = 0,
                ),
                HostData(
                    hostName = "192.168.0.3",
                    ipAddress = "192.168.0.3",
                    detectionTime = 0,
                ),
            ),
            isInit = true,
            onClickBack = {},
            onClickSort = {},
            onClickReload = {},
            onClickItem = {},
            onClickSet = {},
        )
    }
}
