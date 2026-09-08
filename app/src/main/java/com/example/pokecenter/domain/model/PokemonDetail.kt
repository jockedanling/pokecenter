package com.example.pokecenter.domain.model

data class PokemonDetail (
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val types: List<PokemonType>,
    val heightDecimeters: Int,
    val weightHectograms: Int,
    val baseExperience: Int?,
    val abilities: List<String>,
    val stats: List<PokemonStat>,
    val moves: List<String>
) {
    val primaryType: PokemonType
        get() = types.firstOrNull() ?: PokemonType.UNKNOWN
    val formattedNumber: String
        get() = "#${id.toString().padStart(3, '0')}"
    val displayName: String
        get() = name.replaceFirstChar { it.uppercase() }

    // Api:et get height i decimeter och weight i hektogram
    val heightInMeters: Double
        get() = heightDecimeters / 10.00

    val weightInKg: Double
        get() = weightHectograms / 10.00
}