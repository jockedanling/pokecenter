package com.example.pokecenter.domain.model

data class EvolutionChain(
    val stages: List<EvolutionStage>
)
data class EvolutionStage(
    val speciesId: Int,
    val speciesName: String,
    val minLevel: Int? // null, ingen level-krav och bara bas-formen
) {
    val displayName: String
        get() = speciesName.replaceFirstChar { it.uppercase() }

    // Officiella artworken och den byggs direkt från ID och inga extra API-anrop behövs
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$speciesId.png"
}