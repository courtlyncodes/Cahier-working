package com.example.cahier.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
//import androidx.compose.material3.adaptive.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.cahier.R
import com.example.cahier.data.LocalNotesDataProvider
import com.example.cahier.data.Note

@Composable
fun CahierList(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    onClick: () -> Unit,
    viewModel: CahierViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val note = uiState.notes
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
            items(uiState.notesCount) { note ->
                NoteItem(note = uiState.notes[note], onClick = onClick )
            }
        }
    }
}


@OptIn(
    ExperimentalMaterial3AdaptiveNavigationSuiteApi::class,
    ExperimentalMaterial3AdaptiveApi::class
)
@Composable
fun NavigationSuiteHomePane(
    onItemClick: () -> Unit,
    onButtonClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CahierViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val navItems = listOf("Write", "Text", "Photo")
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val customNavSuiteType = with(adaptiveInfo) {
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
            NavigationSuiteType.NavigationDrawer
        } else {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        }
    }


    NavigationSuiteScaffold(
        navigationSuiteItems = {
            navItems.forEachIndexed { index, navItem ->
                item(
                    icon = { Icons.Outlined.CheckCircle },
                    label = { Text(navItem) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        },
        layoutType = customNavSuiteType
    ) {
        Scaffold(
            topBar = {
                CahierTopAppBar()
            },
            floatingActionButton = {
                LargeFloatingActionButton(onClick = onButtonClick) {
                    Icon(Icons.Filled.Add, stringResource(R.string.floating_action_button_des))
                }
            },
            modifier = modifier
        ) { innerPadding ->
            ListDetailPaneScaffoldScreen(
                onClick = { /*TODO*/ },
                onButtonClick = { /*TODO*/ },
                modifier = Modifier.padding(innerPadding)
            )
//            LazyVerticalGrid(
//                columns = GridCells.Adaptive(184.dp),
//                Modifier.padding(innerPadding)
//            )
//            {
//                items(uiState.notesCount) { note ->
//                    NoteItem(note = uiState.notes[note], onClick = onItemClick)
//                }
//            }
        }
    }
}

//Should this go somewhere else?
//class MyItem(val id: Int) {
//    companion object {
//        val Saver: Saver<MyItem?, Int> = Saver(
//            { it?.id },
//            ::MyItem,
//        )
//    }
//}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ListDetailPaneScaffoldScreen(
    onClick: () -> Unit,
    onButtonClick: () -> Unit,
    viewModel: CahierViewModel = viewModel(),
    canvasViewModel: CanvasViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var selectedItem: Note? by rememberSaveable(stateSaver = Note.Saver) {
        mutableStateOf(null)
    }

    val note = LocalNotesDataProvider.allNotes.first()

    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()

    var clicked by remember { mutableStateOf(false) }

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }
    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            CahierList(
                onClick = {
                          selectedItem = note
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                },
                onButtonClick = { navigator.navigateTo(ListDetailPaneScaffoldRole.Extra) },
                viewModel = viewModel
            )

        },
        detailPane = {
//            if (clicked) {
            selectedItem?.let { note ->
                NoteDetail(note)
                navigator.navigateTo(ListDetailPaneScaffoldRole.List)
            }
//            } else {
//                CahierNavHost()
//            }
        },
        extraPane = {
           CahierNavHost()
            navigator.navigateTo(ListDetailPaneScaffoldRole.List)
        }
    )
}
