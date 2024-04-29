package com.example.cahier.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cahier.R
import com.example.cahier.data.Note
import kotlinx.coroutines.launch

@Composable
fun CahierList(
    onButtonClick: () -> Unit,
    onItemClick: (Note) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    Scaffold(
        topBar = {
            CahierTopAppBar()
        },
        floatingActionButton = {
            LargeFloatingActionButton(onClick = onButtonClick) {
                Icon(Icons.Filled.Add, "large floating action button")
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(184.dp),
            Modifier.padding(innerPadding)
        )
        {
            items(homeUiState.noteList.size) {  note ->
                NoteItem(
                    note = homeUiState.noteList[note],
                    onClick = onItemClick
                )
            }
        }
    }
}

enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    HOME(label = R.string.home, icon = Icons.Filled.Home, contentDescription = R.string.home),
    FAVORITES(label = R.string.favorites, icon = Icons.Filled.Favorite, contentDescription = R.string.favorites),
    SETTINGS(label = R.string.settings, icon = Icons.Filled.Settings, contentDescription = R.string.settings),
    TRASH(label = R.string.trash, icon = Icons.Filled.Delete, contentDescription = R.string.trash)
}

@OptIn(ExperimentalMaterial3AdaptiveNavigationSuiteApi::class)
@Composable
fun NavigationSuiteHomeScreen(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = stringResource(it.contentDescription)
                        )
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = currentDestination == it,
                    onClick = { currentDestination = it }
                )
            }
        },
    ) {
        NoteListDetailScreen(onButtonClick = onButtonClick)
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NoteListDetailScreen(
    onButtonClick: () -> Unit,
    viewModel: NoteViewModel = viewModel(),
    daoViewModel: DaoViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val uiState by viewModel.uiState.collectAsState()
//    var currentDetailScreen by rememberSaveable { mutableStateOf(DetailScreen.NOTE_DETAIL) }\


    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            CahierList(
                onItemClick = {
                    daoViewModel.updateUiState(it)
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
//                    currentDetailScreen = DetailScreen.NOTE_DETAIL
                },
                onButtonClick = onButtonClick,
                viewModel = viewModel
            )
        },
        detailPane = {
                    uiState.note?.let { NoteDetail(it) }
            }
    )
}