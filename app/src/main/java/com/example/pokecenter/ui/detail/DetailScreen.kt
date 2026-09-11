package com.example.pokecenter.ui.detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokecenter.domain.model.EvolutionChain
import com.example.pokecenter.domain.model.PokemonDetail
import com.example.pokecenter.domain.model.PokemonStat
import com.example.pokecenter.ui.components.TypeBadge
import com.example.pokecenter.ui.theme.PokeCenterTheme


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    pokemon: PokemonDetail,
    evolutionChain: EvolutionChain? = null,
    isEvolutionLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onLoadEvolution: () -> Unit = {}
) {
    // Vilken tab som är vald (0 = About, 1 = Stats, 2 = Evolution, 3 = Moves)
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("About", "Stats", "Evolution", "Moves")

    Column(modifier = Modifier.fillMaxSize()) {

        // Färgad header med bild
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(pokemon.primaryType.cardBackground)
        ) {
            // Tillbaka-knapp
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(start = 8.dp, top = 40.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            // Nummer i övre högra hörnet
            Text(
                text = pokemon.formattedNumber,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray.copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(end = 16.dp, top = 48.dp)
                    .align(Alignment.TopEnd)
            )

            // Pokémon-bild centrerad
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.displayName,
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit
            )

            // Namn och typ-badges längst ner i headern
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 12.dp)
            ) {
                Text(
                    text = pokemon.displayName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    pokemon.types.forEach { type ->
                        TypeBadge(type = type)
                    }
                }
            }
        }

        // Tabbar
        TabRow(selectedTabIndex = selectedTab) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        // Ladda evolution-data lazy vid första klick
                        if (index == 2) onLoadEvolution()
                    },
                    text = { Text(title) }
                )
            }
        }

        // Tab-innehåll
        when (selectedTab) {
            0 -> AboutTab(pokemon)
            1 -> StatsTab(pokemon.stats, pokemon.primaryType.badgeColor)
            2 -> EvolutionTab(evolutionChain, isEvolutionLoading)
            3 -> MovesTab(pokemon.moves)
        }
    }
}

// About-tab: höjd, vikt, abilities, base experience
@Composable
fun AboutTab(pokemon: PokemonDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        InfoRow(label = "Height", value = "${pokemon.heightInMeters} m")
        InfoRow(label = "Weight", value = "${pokemon.weightInKg} kg")
        InfoRow(label = "Base Exp", value = "${pokemon.baseExperience ?: "—"}")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Abilities",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        pokemon.abilities.forEach { ability ->
            Text(
                text = "• ${ability.replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// Rad med label + värde (för About-tabben)
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

// Stats-tab: animerade stat-bars
@Composable
fun StatsTab(stats: List<PokemonStat>, barColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        stats.forEach { stat ->
            StatBarRow(stat = stat, barColor = barColor)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

// Animerad stat-bar som använder Jockes PokemonStat
@Composable
fun StatBarRow(stat: PokemonStat, barColor: Color) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(stat.value) {
        animatedProgress.animateTo(
            targetValue = stat.precentOfMax,
            animationSpec = tween(durationMillis = 800)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Stat-namn (fast bredd för jämn alignment)
        Text(
            text = stat.shortLabel,
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray,
            modifier = Modifier.width(40.dp)
        )

        // Numeriskt värde
        Text(
            text = stat.value.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(36.dp)
        )

        // Animerad bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = animatedProgress.value)
                    .clip(RoundedCornerShape(4.dp))
                    .background(barColor)
            )
        }
    }
}

// Evolution-tab: kedja med bilder och pilar
@Composable
fun EvolutionTab(
    evolutionChain: EvolutionChain?,
    isLoading: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            evolutionChain == null -> Text("No evolution data")
            else -> {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    evolutionChain.stages.forEachIndexed { index, stage ->
                        // Pokémon i kedjan
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = stage.imageUrl,
                                contentDescription = stage.displayName,
                                modifier = Modifier.size(72.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = stage.displayName,
                                style = MaterialTheme.typography.bodySmall
                            )
                            stage.minLevel?.let { level ->
                                Text(
                                    text = "Lv. $level",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                        }

                        // Pil mellan stegen
                        if (index < evolutionChain.stages.lastIndex) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Evolves to",
                                tint = Color.Gray,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Moves-tab: scrollbar lista med moves
@Composable
fun MovesTab(moves: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(moves) { move ->
            Text(
                text = move.replace("-", " ").replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailScreenPreview() {
    PokeCenterTheme {
        DetailScreen(
            pokemon = DetailPreviewData.pokemon,
            evolutionChain = DetailPreviewData.evolutionChain
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatsTabPreview() {
    PokeCenterTheme {
        StatsTab(
            stats = DetailPreviewData.pokemon.stats,
            barColor = DetailPreviewData.pokemon.primaryType.badgeColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EvolutionTabPreview() {
    PokeCenterTheme {
        EvolutionTab(
            evolutionChain = DetailPreviewData.evolutionChain,
            isLoading = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovesTabPreview() {
    PokeCenterTheme {
        MovesTab(moves = DetailPreviewData.pokemon.moves)
    }
}