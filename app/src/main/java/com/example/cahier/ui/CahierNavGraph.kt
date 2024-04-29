package com.example.cahier.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    val startDestination: String = CahierNavGraph.HOME.name
    val coroutineScope = rememberCoroutineScope()


    Scaffold {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(it)
        ) {
            composable(CahierNavGraph.HOME.name) {
                NavigationSuiteHomeScreen(onButtonClick = {
                    coroutineScope.launch {
                        daoViewModel.resetUiState()
                    }
                    navController.navigate(CahierNavGraph.CANVAS.name)
            } )
            }
            composable(CahierNavGraph.CANVAS.name) {
                CanvasWrapper(
                    canvasUiState = daoViewModel.canvasUiState,
                    onNavigateUp =
                    {
                        coroutineScope.launch {
                            daoViewModel.addNote()
                            navController.navigateUp() }

                    },
                    onValueChange = daoViewModel::updateUiState)
            }
        }
    }
}