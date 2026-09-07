package com.example.pokecenter.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvolutionChainResponse(
    val chain: ChainLinkDto
)
@Serializable
data class ChainLinkDto(
    val species: NamedApiResourceDto,
    @SerialName("evolution_details") val evolutionDetails:
    List<EvolutionDetailDto>,
    @SerialName("evolves_to") val evolvesTo: List<ChainLinkDto>
)
@Serializable
data class EvolutionDetailDto(
    @SerialName("min_level") val minlevel: Int? = null
)