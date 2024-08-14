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
import com.example.cahier.ui.viewmodels.HomeScreenViewModel

object HomeDestination : NavigationDestination {
    override val route = "home"
}

// Navigation Suite items
// Disabled
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
    homeScreenViewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val noteList by homeScreenViewModel.noteList.collectAsState()
    val selectedNote by homeScreenViewModel.uiState.collectAsState()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    // Navigation Suite displays the navigation items
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
        // NSS holds this scaffold to show one or two panes depending on the device's screen size
        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            // Shows the list of notes
            listPane = {
                NoteList(
                    noteList = noteList.noteList,
                    onNoteClick = {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        homeScreenViewModel.selectNote(it.id) /* When a note is clicked, it's id is passed to the view model */
                    },
                    onAddNewNote = {
                        homeScreenViewModel.addNote { it ->
                            navigateToCanvas(it)
                        }
                    }
                )
            },
            // Shows the content of the selected note
            detailPane = {
                selectedNote?.let { it ->
                    NoteDetail(
                        note = it,
                        onDelete = {
                            homeScreenViewModel.deleteNote()
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