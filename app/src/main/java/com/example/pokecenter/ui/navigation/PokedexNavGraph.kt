package com.example.pokecenter.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Compare
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokecenter.ui.detail.DetailScreen
import com.example.pokecenter.ui.detail.DetailViewModel
import com.example.pokecenter.ui.home.HomeScreen
import com.example.pokecenter.ui.theme.PokeCenterTheme
import com.example.pokecenter.ui.favorites.FavoritesScreen
import com.example.pokecenter.ui.favorites.FavoritesViewModel

/**
 Rutter — varje skärm har en unik sträng-adress.
 Detaljvyn har en variabel {pokemonId} som fylls i vid navigation.
 Exempel: detailRoute(25) → "detail/25"
 */
object Routes {
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val COMPARE = "compare"
    const val DETAIL = "detail/{pokemonId}"

    fun detailRoute(pokemonId: Int) = "detail/$pokemonId"
}

/**
  Data-klass för en tab i bottom bar.
  Varje tab har två ikoner — fylld (vald) och outlined (inte vald).
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

// De tre tabbarna som visas i bottom bar
val bottomNavItems = listOf(
    BottomNavItem(
        route = Routes.HOME,
        label = "Pokédex",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        route = Routes.FAVORITES,
        label = "Favorites",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    ),
    BottomNavItem(
        route = Routes.COMPARE,
        label = "Compare",
        selectedIcon = Icons.Filled.Compare,
        unselectedIcon = Icons.Outlined.Compare
    )
)

/**
 * Appens huvudnavigation — kopplar ihop alla skärmar med en bottom bar.

  Struktur:
  Scaffold (hanterar bottom bar + ger innerPadding)
   └── NavHost (bestämmer vilken skärm som visas baserat på rutt)
         ├── HOME → HomeScreen
         ├── FAVORITES → FavoritesScreen (placeholder)
         ├── COMPARE → CompareScreen (placeholder)
         └── DETAIL → DetailScreen (placeholder)
 */
@Composable
fun PokedexNavGraph() {
    // Styrenheten — håller koll på vilken skärm som visas och navigationshistoriken
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Bottom bar döljs på detaljskärmen för att ge mer plats åt innehållet
    val showBottomBar = currentDestination?.route != Routes.DETAIL

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        // Kolla om denna tab är den aktiva genom att jämföra med nuvarande rutt
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Gå tillbaka till startsidan istället för att stacka skärmar
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true  // Spara scroll-position etc.
                                    }
                                    launchSingleTop = true  // Undvik dubbletter vid dubbelklick
                                    restoreState = true     // Återställ state vid tillbaka-navigation
                                }
                            },
                            icon = {
                                // Fylld ikon om vald, outlined annars
                                Icon(
                                    imageVector = if (selected) item.selectedIcon
                                    else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // NavHost bestämmer vilken skärm som renderas baserat på aktuell rutt
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ── Hemskärm ──
            composable(Routes.HOME) {
                HomeScreen(
                    onPokemonClick = { id ->
                        // Klick på kort → navigera till detaljvy med pokemonId
                        navController.navigate(Routes.detailRoute(id))
                    }
                )
            }

//            // Favoriter
//            composable(Routes.FAVORITES) {
//                FavoritesScreen(
//                    favorites = emptyList(), // Ersätts senare med Viewmodel-data
//                    onPokemonClick = { id ->
//                        navController.navigate(Routes.detailRoute(id))
//
//                    }
//                )
//            }
            // Favoriter med viewmodel
            composable(Routes.FAVORITES) {
                val viewModel: FavoritesViewModel = hiltViewModel()
                val favorites by
                viewModel.favorites.collectAsStateWithLifecycle()
                FavoritesScreen(
                    favorites = favorites,
                    onPokemonClick = { id ->
                        navController.navigate(Routes.detailRoute(id))
                    },
                    onRemoveFavorite = viewModel::removeFavorite
                )
            }

            // Compare — ersätts med CompareScreen senare
            composable(Routes.COMPARE) {
                PlaceholderScreen(title = "Compare")
            }

            // Detaljvy — tar emot pokemonId från rutten
            composable(
                route = Routes.DETAIL,
                arguments = listOf(
                    navArgument("pokemonId") { type = NavType.IntType }
                )
            ) {
                val viewModel: DetailViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                when {
                    uiState.isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    uiState.error != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = uiState.error ?: "Something went wrong")
                        }
                    }
                    uiState.pokemon != null -> {
                        DetailScreen(pokemon = uiState.pokemon!!,
                            evolutionChain = uiState.evolutionChain,
                            isEvolutionLoading = uiState.isEvolutionLoading,
                            onBackClick = { navController.popBackStack()},
                            onLoadEvolution = {viewModel.loadEvolutionChain()})
                    }
                }
                /*backStackEntry ->
                // Plocka ut ID: "detail/25" → pokemonId = 25
                val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: return@composable
                PlaceholderScreen(title = "Detail #$pokemonId") */
            }
        }
    }
}

/**
Tillfällig platshållar-skärm.
Visas för tabs som inte byggts klart ännu (Favorites, Compare, Detail).
Ersätts med riktiga skärmar efterhand.
 */
@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NavGraphPreview() {
    PokeCenterTheme {
        PokedexNavGraph()
    }
}
 */