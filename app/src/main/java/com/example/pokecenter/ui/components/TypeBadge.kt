package com.example.pokecenter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokecenter.domain.model.PokemonType
import com.example.pokecenter.ui.theme.PokeCenterTheme

// Liten färgad chip som visar en Pokémon-typ (t.ex. "Fire", "Water")
@Composable
fun TypeBadge(
    type: PokemonType,
    modifier: Modifier = Modifier
) {
    Text(
        text = type.displayName,
        style = MaterialTheme.typography.labelSmall,
        color = Color.White,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(type.badgeColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

// Preview — syns bara i Android Studio, inte i appen
@Preview(showBackground = true)
@Composable
fun TypeBadgePreview() {
    PokeCenterTheme {
        TypeBadge(type = PokemonType.FIRE)
    }
}