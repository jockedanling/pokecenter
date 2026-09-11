package com.example.pokecenter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokecenter.domain.model.Pokemon
import com.example.pokecenter.domain.model.PokemonType
import com.example.pokecenter.ui.theme.PokeCenterTheme

// Pokémon-kort som visas i grid på hemskärmen och favoriter
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = pokemon.primaryType.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding( horizontal = 12.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pokédex-nummer i övre högra hörnet
            Text(
                text = pokemon.formattedNumber,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )

            // Pokémon-bild (laddas från URL via Coil)
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.displayName,
                modifier = Modifier
                    .size(96.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )

            // Pokémon-namn
            Text(
                text = pokemon.displayName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Typ-badges (använder TypeBadge-komponenten)
            FlowRow(
                modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                pokemon.types.forEach { type ->
                    TypeBadge(type = type)
                }
            }
        }
    }
}

// Preview med hårdkodad testdata
@Preview(showBackground = true)
@Composable
fun PokemonCardPreview() {
    PokeCenterTheme {
        PokemonCard(
            pokemon = Pokemon(
                id = 25,
                name = "pikachu",
                imageUrl = null,
                types = listOf(PokemonType.ELECTRIC)
            ),
            onClick = {}
        )
    }
}