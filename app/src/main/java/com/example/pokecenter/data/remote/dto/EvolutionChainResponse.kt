package com.example.pokecenter.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
/*
Svar från GET /evolution-chain/{id}.
 */
@Serializable
data class EvolutionChainResponse(
    val chain: ChainLinkDto
)
// Ett steg i evolutionskedjan.
// Strukturen är rekursiv: varje steg har en lista
// evolvesTo med nästa steg, tex: Bulbasaur -> Ivysaur -> Venusaur.
// Appen följer bara första grenen
@Serializable
data class ChainLinkDto(
    val species: NamedApiResourceDto,
    @SerialName("evolution_details") val evolutionDetails:
    List<EvolutionDetailDto>,
    @SerialName("evolves_to") val evolvesTo: List<ChainLinkDto>
)
@Serializable
data class EvolutionDetailDto(
    @SerialName("min_level") val minLevel: Int? = null
)