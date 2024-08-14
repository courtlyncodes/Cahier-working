package com.example.cahier.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cahier.ui.HomeDestination
import com.example.cahier.ui.HomePane
import com.example.cahier.ui.NoteCanvas
import com.example.cahier.ui.NoteCanvasDestination


@Composable
fun CahierNavHost(
    navController: NavHostController
) {
    val startDestination: String = HomeDestination.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(HomeDestination.route) {
            HomePane(
                navigateToCanvas = {
                    // Navigate to the note canvas screen with the noteId argument
                    navController.navigate("${NoteCanvasDestination.route}/${it}")
                },
                navigateUp = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }
        composable(
            route = NoteCanvasDestination.routeWithArgs,
            // Add the noteId argument to the route to navigate to a specific note
            arguments = listOf(navArgument(NoteCanvasDestination.NOTE_ID_ARG) {
                type = NavType.LongType
            })
        ) {
            NoteCanvas(
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}