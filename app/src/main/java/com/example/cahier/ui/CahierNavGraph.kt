package com.example.cahier.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cahier.data.LocalNotesDataProvider

enum class CahierNavGraph (){
    CANVAS
}


@Composable
fun CahierNavHost(
    viewModel: CahierViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),

) {
    val startDestination: String = CahierNavGraph.CANVAS.name

    Scaffold() { it ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(it)
        ) {
            composable(CahierNavGraph.CANVAS.name) {
                PracticeCanvas()
            }
        }
    }
}