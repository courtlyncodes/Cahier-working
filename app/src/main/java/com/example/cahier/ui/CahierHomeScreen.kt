package com.example.cahier.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cahier.R
import com.example.cahier.data.Note
import com.example.cahier.ui.viewmodels.AppViewModelProvider
import com.example.cahier.ui.viewmodels.NoteDetailViewModel
import com.example.cahier.ui.viewmodels.NoteListViewModel
import kotlinx.coroutines.launch


enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    HOME(
        label = R.string.home,
        icon = Icons.Filled.Home,
        contentDescription = R.string.home
    ),
    FAVORITES(
        label = R.string.favorites,
        icon = Icons.Filled.Favorite,
        contentDescription = R.string.favorites
    ),
    SETTINGS(
        label = R.string.settings,
        icon = Icons.Filled.Settings,
        contentDescription = R.string.settings
    ),
    TRASH(
        label = R.string.trash,
        icon = Icons.Filled.Delete,
        contentDescription = R.string.trash
    )
}

@OptIn(ExperimentalMaterial3AdaptiveNavigationSuiteApi::class)
@Composable
fun HomeScreen(
    navigateToCanvas: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    noteDetailViewModel: NoteDetailViewModel
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    val coroutineScope = rememberCoroutineScope()
    var newlyAddedId: Long
    val uiState by noteDetailViewModel.uiState.collectAsState()

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
        NoteListAndDetailPane(
            onAddNewNote = {
                try {
                    coroutineScope.launch {
                        noteDetailViewModel.resetUiState()
                        newlyAddedId = 0L
                        newlyAddedId = noteDetailViewModel.addNote()
                        noteDetailViewModel.updateUiState(uiState.note.copy(id = newlyAddedId))
                    }
                    navigateToCanvas()
                } catch (e: Exception) {
                    Log.e("error", "Failed to add note: ${e.message}")
                }

            },
            onClickToEdit = {
                noteDetailViewModel.updateUiState(it.copy())
                navigateToCanvas()
            },
            onDelete = {
                coroutineScope.launch {
                    noteDetailViewModel.deleteNote()
                }
                navigateUp()
            },
            noteDetailViewModel = noteDetailViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NoteListAndDetailPane(
    onAddNewNote: () -> Unit,
    onClickToEdit: (Note) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    noteDetailViewModel: NoteDetailViewModel,
    noteListViewModel: NoteListViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val noteList by noteListViewModel.noteList.collectAsState()
    val uiState by noteDetailViewModel.uiState.collectAsState()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            NoteList(
                noteList = noteList.noteList,
                onNoteClick = {
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    noteDetailViewModel.updateUiState(it.copy())
                },
                onAddNewNote = onAddNewNote
            )
        },
        detailPane = {
            NoteDetail(
                note = uiState.note,
                onDelete = onDelete,
                onClickToEdit = onClickToEdit
            )
        }
    )
}