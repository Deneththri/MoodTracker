package com.example.moodtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moodtracker.ui.Screen.DetailScreen
import com.example.moodtracker.ui.Screen.MainScreen
import com.example.moodtracker.ui.Screen.ReportScreen
import com.example.moodtracker.ui.theme.MoodTrackerTheme
import com.example.moodtracker.viewmodel.MoodViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MoodTrackerApp()
                }
            }
        }
    }
}

@Composable
fun MoodTrackerApp() {
    val navController = rememberNavController()
    val viewModel: MoodViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                viewModel = viewModel,
                onNavigateToDetail = { moodId ->
                    navController.navigate("detail/$moodId")
                },
                onNavigateToReport = {
                    navController.navigate("report")
                }
            )
        }

        composable(
            route = "detail/{moodId}",
            arguments = listOf(navArgument("moodId") { type = NavType.LongType })
        ) { backStackEntry ->
            val moodId = backStackEntry.arguments?.getLong("moodId") ?: 0L
            DetailScreen(
                viewModel = viewModel,
                moodId = moodId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("report") {
            ReportScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}