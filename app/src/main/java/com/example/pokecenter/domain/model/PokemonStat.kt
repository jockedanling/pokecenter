package com.example.pokecenter.domain.model

data class PokemonStat(
    val name: String, // "hp, "attack, "special-attack osv från API:et

    val value: Int
)
{
    val shortLabel: String
        get() = when (name) {
            "hp" -> "HP"
            "attack" -> "ATK"
            "defense" -> "DEF"
            "special-attack" -> "spA"
            "special-defense" -> "spD"
            "speed" -> "SPD"
            else -> name
        }

    // Bas-stats går 1-255, användbart för StatBar-fyllnadsgrad
    val precentOfMax: Float
        get() = (value / 255f).coerceIn(0f, 1f)
}