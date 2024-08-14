package com.example.cahier.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cahier.navigation.CahierNavHost

/**
 * Top level composable that represents screens for the application.
 */
@Composable
fun CahierApp(navController: NavHostController = rememberNavController()) {
    CahierNavHost(navController = navController)
}