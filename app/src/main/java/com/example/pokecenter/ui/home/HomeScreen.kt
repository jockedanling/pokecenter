package com.example.pokecenter.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokecenter.domain.model.PokemonType
import com.example.pokecenter.ui.components.PokemonCard
import com.example.pokecenter.ui.components.PokemonSearchBar
import com.example.pokecenter.ui.theme.PokeCenterTheme

/**
 * Home Screen visar sökfält, typfilter och Pokémon-grid.
 * Bygger mot PreviewData tills ViewModel kopplas in.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPokemonClick: (Int) -> Unit = {}
) {
    // Tillfälligt lokalt state — ersätts av ViewModel senare
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<PokemonType?>(null) }

    // Filtrera testdatan baserat på sök och typfilter
    val filteredPokemon = PreviewData.pokemonList.filter { pokemon ->
        val matchesSearch = pokemon.name.contains(searchQuery, ignoreCase = true)
        val matchesType = selectedType == null || pokemon.types.contains(selectedType)
        matchesSearch && matchesType
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // App-titel
        Text(
            text = "Pokédex",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(start = 16.dp, top = 40.dp, bottom = 8.dp)
        )

        // Sökfält
        PokemonSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        // Typfilter-chips — horisontell scrollbar rad
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(PokemonType.entries.filter { it != PokemonType.UNKNOWN }) { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = {
                        // Tryck igen = avmarkera
                        selectedType = if (selectedType == type) null else type
                    },
                    label = { Text(type.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = type.badgeColor,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Pokémon-grid — 2 kolumner
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = filteredPokemon,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    PokeCenterTheme {
        HomeScreen()
    }
}