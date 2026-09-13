package com.example.pokecenter.data.remote.dto

import kotlinx.serialization.Serializable

/*
Svar från GET /pokemon, en sida av pokémon-listan.
*/
@Serializable
data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItemDto>
)

// En pokemon i listan. Id:t finns bara i URL:en.
@Serializable
data class PokemonListItemDto(
    val name: String,
    val url: String
)