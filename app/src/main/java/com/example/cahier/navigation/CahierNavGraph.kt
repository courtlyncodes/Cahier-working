package com.example.cahier.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cahier.ui.viewmodels.AppViewModelProvider
import com.example.cahier.ui.viewmodels.DaoViewModel
import kotlinx.coroutines.launch

enum class CahierNavGraph {
    HOME,
    CANVAS
}

@Composable
fun CahierNavHost(
    daoViewModel: DaoViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController(),
) {
    val uiState = daoViewModel.uiState.collectAsState()
//    var itemId =
    val startDestination: String = CahierNavGraph.HOME.name
    val coroutineScope = rememberCoroutineScope()
    val note by daoViewModel.note.collectAsState()



    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(CahierNavGraph.HOME.name) {
            HomeScreen(
                onButtonClick = {
                    daoViewModel.resetUiState()
                    navController.navigate(CahierNavGraph.CANVAS.name)
                },
                onEditNote = {
                    daoViewModel.updateUiState(it)
                    navController.navigate(CahierNavGraph.CANVAS.name)
                }
                    )
        }
        composable(CahierNavGraph.CANVAS.name) {
            NoteCanvas(
                note = uiState.value.note,
                onNavigateUp = {
                    coroutineScope.launch {
                        if (uiState.value.note.id == 0.toLong()) {
                            daoViewModel.addNote()
                        }
                        else {
                            daoViewModel.updateNote()
                            daoViewModel.updateUiState(uiState.value.note)
                            Log.wtf("cahier nav graph", "onNavigateUp: ${daoViewModel.currentNoteId.value}")
                        }
                    }

                    navController.navigateUp()
                },
                onValueChange = {
                    daoViewModel.updateUiState(it)
                }
            )
        }
    }
}
