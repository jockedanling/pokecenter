package com.example.pokecenter.ui.compare

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokecenter.domain.model.PokemonDetail
import com.example.pokecenter.domain.model.PokemonStat
import com.example.pokecenter.domain.model.PokemonType
import com.example.pokecenter.ui.components.TypeBadge
import com.example.pokecenter.ui.theme.PokeCenterTheme


@Composable
fun CompareScreen(
    firstPokemon: PokemonDetail? = null,
    secondPokemon: PokemonDetail? = null,
    statComparison: List<StatComparison> = emptyList(),
    onSelectFirst: (Int) -> Unit = {},
    onSelectSecond: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp)
    ) {
        // Titel
        Text(
            text = "Compare",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )

        // Sökfält för att välja Pokémon via ID
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            var firstId by remember { mutableStateOf("") }
            var secondId by remember { mutableStateOf("") }

            OutlinedTextField(
                value = firstId,
                onValueChange = {
                    firstId = it
                    it.toIntOrNull()?.let { id -> onSelectFirst(id) }
                },
                label = { Text("Pokémon #") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            OutlinedTextField(
                value = secondId,
                onValueChange = {
                    secondId = it
                    it.toIntOrNull()?.let { id -> onSelectSecond(id) }
                },
                label = { Text("Pokémon #") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        // Två Pokémon-kort sida vid sida
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CompareSlot(
                pokemon = firstPokemon,
                placeholder = "Select first",
                modifier = Modifier.weight(1f)
            )
            CompareSlot(
                pokemon = secondPokemon,
                placeholder = "Select second",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats-jämförelse som visas bara när båda är valda
        if (statComparison.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(statComparison) { stat ->
                    CompareStatRow(stat = stat)
                }
            }
        }
    }
}

// En slot som visar en vald Pokémon eller en tom platshållare
@Composable
fun CompareSlot(
    pokemon: PokemonDetail?,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                pokemon?.primaryType?.cardBackground ?: Color.LightGray.copy(alpha = 0.3f)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (pokemon != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(12.dp)
            ) {
                // Pokémon-bild
                AsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = pokemon.displayName,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )

                // Namn
                Text(
                    text = pokemon.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Nummer
                Text(
                    text = pokemon.formattedNumber,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                // Typ-badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    pokemon.types.forEach { type ->
                        TypeBadge(type = type)
                    }
                }
            }
        } else {
            // Tom platshållare
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}

// En rad i stats-jämförelsen med bars åt båda håll
@Composable
fun CompareStatRow(stat: StatComparison) {
    Column {
        // Stat-namn centrerat
        Text(
            text = stat.statLabel,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Vänster värde
            Text(
                text = stat.valueA.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (stat.aIsHigher) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.width(36.dp),
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Vänster bar (växer åt höger)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = stat.valueA / 255f)
                        .clip(RoundedCornerShape(5.dp))
                        .background(
                            if (stat.aIsHigher) Color(0xFF4CAF50)
                            else Color(0xFFBDBDBD)
                        )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Höger bar (växer åt höger)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = stat.valueB / 255f)
                        .clip(RoundedCornerShape(5.dp))
                        .background(
                            if (stat.bIsHigher) Color(0xFF4CAF50)
                            else Color(0xFFBDBDBD)
                        )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Höger värde
            Text(
                text = stat.valueB.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (stat.bIsHigher) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.width(36.dp)
            )
        }
    }
}

// Preview med två Pokémon
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CompareScreenPreview() {
    val pikachu = PokemonDetail(
        id = 25, speciesId = 25, name = "pikachu", imageUrl = null,
        types = listOf(PokemonType.ELECTRIC),
        heightDecimeters = 4, weightHectograms = 60,
        baseExperience = 112, abilities = listOf("static"),
        stats = listOf(
            PokemonStat("hp", 35), PokemonStat("attack", 55),
            PokemonStat("defense", 40), PokemonStat("special-attack", 50),
            PokemonStat("special-defense", 50), PokemonStat("speed", 90)
        ),
        moves = emptyList()
    )
    val charizard = PokemonDetail(
        id = 6, speciesId = 6, name = "charizard", imageUrl = null,
        types = listOf(PokemonType.FIRE, PokemonType.FLYING),
        heightDecimeters = 17, weightHectograms = 905,
        baseExperience = 267, abilities = listOf("blaze"),
        stats = listOf(
            PokemonStat("hp", 78), PokemonStat("attack", 84),
            PokemonStat("defense", 78), PokemonStat("special-attack", 109),
            PokemonStat("special-defense", 85), PokemonStat("speed", 100)
        ),
        moves = emptyList()
    )

    PokeCenterTheme {
        CompareScreen(
            firstPokemon = pikachu,
            secondPokemon = charizard,
            statComparison = pikachu.stats.zip(charizard.stats) { a, b ->
                StatComparison(a.shortLabel, a.value, b.value)
            }
        )
    }
}

// Preview med tomma slots
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CompareEmptyPreview() {
    PokeCenterTheme {
        CompareScreen()
    }
}