package com.example.cahier.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    val startDestination: String = CahierNavGraph.HOME.name
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(CahierNavGraph.HOME.name) {
            HomeScreen(
                onButtonClick = {
                    coroutineScope.launch {
                        daoViewModel.resetUiState()
                    }
                    navController.navigate(CahierNavGraph.CANVAS.name) })
        }
        composable(CahierNavGraph.CANVAS.name) {
            NoteCanvas(
                note = uiState.value.note,
                onNavigateUp = {
                    coroutineScope.launch {
                        daoViewModel.addNote()
                        navController.navigateUp() }

                },
                onValueChange = {daoViewModel.updateNote(it)}
            )
        }
            }
            }
