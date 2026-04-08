package org.bssm.studentidreplica.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.bssm.studentidreplica.feature.history.detail.DetailScreen
import org.bssm.studentidreplica.feature.history.detail.HistoryDetailViewModel
import org.bssm.studentidreplica.feature.history.list.HistoryListScreen
import org.bssm.studentidreplica.feature.history.list.HistoryListViewModel
import org.bssm.studentidreplica.feature.home.HomeScreen
import org.bssm.studentidreplica.feature.home.HomeUiState
import org.bssm.studentidreplica.feature.home.HomeViewModel

private object Route {
    const val Home = "home"
    const val History = "history"
    const val Detail = "detail/{tagId}"

    fun detail(tagId: Long): String = "detail/$tagId"
}

@Composable
fun StudentIdReplicaApp(
    homeViewModel: HomeViewModel,
    homeUiState: HomeUiState,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.Home) {
        composable(Route.Home) {
            HomeScreen(
                state = homeUiState,
                onStartReading = homeViewModel::startReading,
                onOpenHistory = { navController.navigate(Route.History) },
                onAcceptConsent = homeViewModel::acceptConsent,
            )
        }
        composable(Route.History) {
            val viewModel: HistoryListViewModel = hiltViewModel()
            val records by viewModel.records.collectAsStateWithLifecycle()
            HistoryListScreen(
                records = records,
                onOpenDetail = { tagId -> navController.navigate(Route.detail(tagId)) }
            )
        }
        composable(
            route = Route.Detail,
            arguments = listOf(navArgument("tagId") { type = NavType.LongType })
        ) {
            val viewModel: HistoryDetailViewModel = hiltViewModel()
            val record by viewModel.record.collectAsStateWithLifecycle()
            DetailScreen(record = record)
        }
    }
}
