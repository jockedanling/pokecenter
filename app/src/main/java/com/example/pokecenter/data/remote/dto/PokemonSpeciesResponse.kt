package com.example.pokecenter.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/* Svar från GET /pokemon-species/{id}.
Används bara för att få fram URL:en till evolutionskedjan.
* */
@Serializable
data class PokemonSpeciesResponse(
    @SerialName("evolution_chain") val evolutionChain: EvolutionChainLinkDto
)

@Serializable
data class EvolutionChainLinkDto(
    val url: String
)