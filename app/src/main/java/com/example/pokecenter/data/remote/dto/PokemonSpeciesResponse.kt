package com.example.pokecenter.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonSpeciesResponse(
    @SerialName("flavor_text_entries") val flavorTextEntries:
    List<FlavorTextEntryDto>,
    @SerialName("evolution_chain") val evolutionChain: EvolutionChainLinkDto
)

@Serializable
data class FlavorTextEntryDto(
    @SerialName("flavor_text") val flavorText: String,
    val language: NamedApiResourceDto,
    val version: NamedApiResourceDto
)

@Serializable
data class EvolutionChainLinkDto(
    val url: String
)