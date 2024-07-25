package com.example.cahier.ui

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
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cahier.R
import com.example.cahier.navigation.NavigationDestination
import com.example.cahier.ui.viewmodels.AppViewModelProvider
import com.example.cahier.ui.viewmodels.NoteListViewModel

object HomeDestination : NavigationDestination {
    override val route = "home"
}

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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomePane(
    navigateToCanvas: (Long) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    noteListViewModel: NoteListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val noteList by noteListViewModel.noteList.collectAsState()
    val selectedNote by noteListViewModel.uiState.collectAsState()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

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
        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                NoteList(
                    noteList = noteList.noteList,
                    onNoteClick = {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        noteListViewModel.selectNote(it.id)
                    },
                    onAddNewNote = {
                        noteListViewModel.addNote { it ->
                            navigateToCanvas(it)
                        }
                    }
                )
            },
            detailPane = {
                selectedNote?.let { it ->
                    NoteDetail(
                        note = it,
                        onDelete = {
                            noteListViewModel.deleteNote()
                            navigateUp()
                        },
                        onClickToEdit = {
                            navigateToCanvas(it.id)
                        }
                    )
                }
            }
        )
    }
}