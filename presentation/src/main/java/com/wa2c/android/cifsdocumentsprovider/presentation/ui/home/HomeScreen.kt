package com.wa2c.android.cifsdocumentsprovider.presentation.ui.home

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wa2c.android.cifsdocumentsprovider.common.utils.logD
import com.wa2c.android.cifsdocumentsprovider.common.values.StorageType
import com.wa2c.android.cifsdocumentsprovider.domain.model.RemoteConnectionIndex
import com.wa2c.android.cifsdocumentsprovider.presentation.R
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.AppSnackbarHost
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.IconTile
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.PillLabel
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.PopupMessageType
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.Theme
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.getAppTopAppBarColors
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.iconRes
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.shortLabel
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.showError
import com.wa2c.android.cifsdocumentsprovider.presentation.ui.common.showPopup
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState


/**
 * Home Screen
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenFile: (uris: List<Uri>) -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateEdit: (RemoteConnectionIndex?) -> Unit,
    onNavigateHost: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val fileOpenLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        logD(uris)
        onOpenFile(uris)
    }
    val connectionList = viewModel.connectionListFlow.collectAsStateWithLifecycle(emptyList())

    HomeScreenContainer(
        snackbarHostState = snackbarHostState,
        connectionList = connectionList.value,
        onClickMenuOpenFile = {
            if (connectionList.value.isNotEmpty()) {
                try {
                    fileOpenLauncher.launch(arrayOf("*/*"))
                } catch (e: Exception) {
                    scope.showError(snackbarHostState, e)
                }
            } else {
                scope.showPopup(
                    snackbarHostState = snackbarHostState,
                    stringRes = R.string.home_open_file_ng_message,
                    type = PopupMessageType.Warning,
                )
            }
        },
        onClickMenuSettings = { onNavigateSettings() },
        onClickItem = { onNavigateEdit(it) },
        onClickAddItem = { onNavigateEdit(null) },
        onClickSearchItem = { onNavigateHost() },
        onDragAndDrop = { from, to -> viewModel.onItemMove(from, to) },
    )
}

/**
 * Home Screen Container
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContainer(
    snackbarHostState: SnackbarHostState,
    connectionList: List<RemoteConnectionIndex>,
    onClickMenuOpenFile: () -> Unit,
    onClickMenuSettings: () -> Unit,
    onClickItem: (RemoteConnectionIndex) -> Unit,
    onClickAddItem: () -> Unit,
    onClickSearchItem: () -> Unit,
    onDragAndDrop: (from: Int, to: Int) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    var showAddSheet by remember { mutableStateOf(false) }
    val fabExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                colors = getAppTopAppBarColors(),
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = onClickMenuOpenFile) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_folder_open),
                            contentDescription = stringResource(id = R.string.home_open_file),
                        )
                    }
                    IconButton(onClick = onClickMenuSettings) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
                            contentDescription = stringResource(id = R.string.home_open_settings),
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (connectionList.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    text = { Text(stringResource(id = R.string.ui_home_add_connection)) },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
                            contentDescription = null,
                        )
                    },
                    expanded = fabExpanded,
                    onClick = { showAddSheet = true },
                )
            }
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (connectionList.isEmpty()) {
                EmptyState(
                    onClickSearch = onClickSearchItem,
                    onClickInput = onClickAddItem,
                )
            } else {
                ConnectionList(
                    listState = listState,
                    connectionList = connectionList,
                    onClickItem = onClickItem,
                    onDragAndDrop = onDragAndDrop,
                )
            }
        }
    }

    if (showAddSheet) {
        AddConnectionSheet(
            onDismiss = { showAddSheet = false },
            onClickSearch = onClickSearchItem,
            onClickInput = onClickAddItem,
        )
    }
}

/**
 * Empty state
 */
@Composable
private fun EmptyState(
    onClickSearch: () -> Unit,
    onClickInput: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Theme.Sizes.LL, vertical = Theme.Sizes.L),
    ) {
        IconTile(
            iconRes = R.drawable.ic_lan,
            size = 112.dp,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Spacer(Modifier.height(Theme.Sizes.L))
        Text(
            text = stringResource(id = R.string.ui_home_empty_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Theme.Sizes.S))
        Text(
            text = stringResource(id = R.string.ui_home_empty_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Theme.Sizes.LL))
        Button(
            onClick = onClickSearch,
            contentPadding = PaddingValues(horizontal = Theme.Sizes.L, vertical = 14.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(Theme.Sizes.S))
            Text(stringResource(id = R.string.home_add_connection_search))
        }
        Spacer(Modifier.height(Theme.Sizes.S))
        FilledTonalButton(
            onClick = onClickInput,
            contentPadding = PaddingValues(horizontal = Theme.Sizes.L, vertical = 14.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(Theme.Sizes.S))
            Text(stringResource(id = R.string.home_add_connection_input))
        }
    }
}

/**
 * Bottom sheet to choose how to add a connection.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddConnectionSheet(
    onDismiss: () -> Unit,
    onClickSearch: () -> Unit,
    onClickInput: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val close: (() -> Unit) -> Unit = { action ->
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            onDismiss()
            action()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = Theme.Sizes.M)
        ) {
            Text(
                text = stringResource(id = R.string.ui_home_add_connection),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = Theme.Sizes.L, vertical = Theme.Sizes.S),
            )
            SheetOption(
                iconRes = R.drawable.ic_search,
                title = stringResource(id = R.string.home_add_connection_search),
                description = stringResource(id = R.string.ui_home_add_search_desc),
                onClick = { close(onClickSearch) },
            )
            SheetOption(
                iconRes = R.drawable.ic_edit,
                title = stringResource(id = R.string.home_add_connection_input),
                description = stringResource(id = R.string.ui_home_add_input_desc),
                onClick = { close(onClickInput) },
            )
        }
    }
}

@Composable
private fun SheetOption(
    iconRes: Int,
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(title, style = MaterialTheme.typography.titleMedium) },
        supportingContent = { Text(description) },
        leadingContent = {
            IconTile(
                iconRes = iconRes,
                size = 48.dp,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier
            .padding(horizontal = Theme.Sizes.M, vertical = Theme.Sizes.SS)
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick),
    )
}

/**
 * Connection List
 */
@Composable
fun ConnectionList(
    listState: androidx.compose.foundation.lazy.LazyListState,
    connectionList: List<RemoteConnectionIndex>,
    onClickItem: (RemoteConnectionIndex) -> Unit,
    onDragAndDrop: (from: Int, to: Int) -> Unit,
) {
    val state = rememberReorderableLazyListState(
        lazyListState = listState,
        onMove = { from, to ->
            onDragAndDrop(from.index, to.index)
        }
    )
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(
            start = Theme.Sizes.M,
            end = Theme.Sizes.M,
            top = Theme.Sizes.S,
            bottom = 96.dp, // space for FAB
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(items = connectionList, { it }) { connection ->
            ReorderableItem(state, key = connection) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 8.dp else 0.dp, label = "drag")
                ConnectionItem(
                    connection = connection,
                    elevation = elevation.value,
                    modifier = Modifier.longPressDraggableHandle(),
                    onClick = { onClickItem(connection) },
                )
            }
        }
        item(key = "hint") {
            Text(
                text = stringResource(id = R.string.ui_home_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Theme.Sizes.S),
            )
        }
    }
}

@Composable
private fun ConnectionItem(
    connection: RemoteConnectionIndex,
    elevation: androidx.compose.ui.unit.Dp,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(Theme.Sizes.M),
        ) {
            IconTile(
                iconRes = connection.storage.iconRes,
                size = 48.dp,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Theme.Sizes.M)
            ) {
                Text(
                    text = connection.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = connection.uri,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            PillLabel(text = connection.storage.shortLabel)
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
private fun HomeScreenContainerPreview() {
    Theme.AppTheme {
        HomeScreenContainer(
            snackbarHostState = SnackbarHostState(),
            connectionList = listOf(
                RemoteConnectionIndex(
                    id = "",
                    name = "NAS",
                    storage = StorageType.SMBJ,
                    uri = "smb://192.168.0.10/daten/"
                ),
                RemoteConnectionIndex(
                    id = "",
                    name = "Büro-PC",
                    storage = StorageType.JCIFS,
                    uri = "smb://buero-pc/freigabe/",
                ),
            ),
            onClickItem = {},
            onClickAddItem = {},
            onClickSearchItem = {},
            onDragAndDrop = { _, _ -> },
            onClickMenuOpenFile = {},
            onClickMenuSettings = {},
        )
    }
}

@Preview(
    name = "Empty",
    group = "Group",
    showBackground = true,
)
@Composable
private fun HomeScreenEmptyPreview() {
    Theme.AppTheme {
        HomeScreenContainer(
            snackbarHostState = SnackbarHostState(),
            connectionList = emptyList(),
            onClickItem = {},
            onClickAddItem = {},
            onClickSearchItem = {},
            onDragAndDrop = { _, _ -> },
            onClickMenuOpenFile = {},
            onClickMenuSettings = {},
        )
    }
}
