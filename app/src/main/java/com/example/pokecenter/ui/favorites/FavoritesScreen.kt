package com.example.pokecenter.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokecenter.domain.model.Pokemon
import com.example.pokecenter.domain.model.PokemonType
import com.example.pokecenter.ui.components.PokemonCard
import com.example.pokecenter.ui.theme.PokeCenterTheme


@Composable
fun FavoritesScreen(
    favorites: List<Pokemon>,
    onPokemonClick: (Int) -> Unit = {},
    onRemoveFavorite: (Int) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Titel
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(start = 16.dp, top = 48.dp, bottom = 16.dp)
        )

        if (favorites.isEmpty()) {
            // Tom-state — visas när inga favoriter sparats
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorites yet — tap the heart on a Pokémon to save it!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            // Samma grid-layout som hemskärmen
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = favorites,
                    key = { it.id }
                ) { pokemon ->
                    PokemonCard(
                        pokemon = pokemon,
                        onClick = { onPokemonClick(pokemon.id) }
                    )
                }
            }
        }
    }
}

// Preview med fejkdata
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavoritesScreenPreview() {
    PokeCenterTheme {
        FavoritesScreen(
            favorites = listOf(
                Pokemon(25, "pikachu", null, listOf(PokemonType.ELECTRIC)),
                Pokemon(6, "charizard", null, listOf(PokemonType.FIRE, PokemonType.FLYING)),
                Pokemon(150, "mewtwo", null, listOf(PokemonType.PSYCHIC))
            )
        )
    }
}

// Preview med tom lista
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavoritesEmptyPreview() {
    PokeCenterTheme {
        FavoritesScreen(favorites = emptyList())
    }
}