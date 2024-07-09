package com.example.cahier.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cahier.ui.HomeScreen
import com.example.cahier.ui.NoteCanvas
import com.example.cahier.ui.viewmodels.AppViewModelProvider
import com.example.cahier.ui.viewmodels.NoteDetailViewModel

enum class CahierNavGraph {
    HOME,
    CANVAS
}

@Composable
fun CahierNavHost(
    navController: NavHostController = rememberNavController(),
    noteDetailViewModel: NoteDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val startDestination: String = CahierNavGraph.HOME.name

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(CahierNavGraph.HOME.name) {
            HomeScreen(
                navigateToCanvas = {
                    navController.navigate(CahierNavGraph.CANVAS.name)
                },
                navigateUp = {
                    navController.navigate(CahierNavGraph.HOME.name)
                },
                noteDetailViewModel = noteDetailViewModel
            )
        }
        composable(CahierNavGraph.CANVAS.name) {
            NoteCanvas(
                navigateUp = {
                    navController.navigateUp()
                },
                noteDetailViewModel = noteDetailViewModel
            )
        }
    }
}
