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
import com.example.cahier.ui.viewmodels.DaoViewModel
import com.example.cahier.ui.viewmodels.NotesListViewModel
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
    onButtonClick: () -> Unit,
    onEditNote: (Note) -> Unit,
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
        NoteListAndDetailPane(onButtonClick = onButtonClick, onEditNote = onEditNote)
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NoteListAndDetailPane(
    onButtonClick: () -> Unit,
    onEditNote: (Note) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DaoViewModel = viewModel(factory = AppViewModelProvider.Factory),
    notesListViewModel: NotesListViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val notesList by notesListViewModel.noteList.collectAsState()
    val note by viewModel.note.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            NoteList(
                noteList = notesList.noteList,
                onItemClick = {
                    viewModel.setCurrentNoteId(it.id)
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                },
                onButtonClick = onButtonClick
            )
        },
        detailPane = {
                NoteDetail(
                    note = note.note,
                    onDelete = {
                            coroutineScope.launch {
                                viewModel.deleteNote()
                                navigator.navigateBack()
                            }
                               },
                    onEditNote = onEditNote
                )
        }
    )
}