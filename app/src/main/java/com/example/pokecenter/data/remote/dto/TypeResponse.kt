package com.example.pokecenter.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TypeResponse(
    val pokemon: List<TypePokemonSlotDto>
)
@Serializable
data class TypePokemonSlotDto(
    val pokemon: NamedApiResourceDto,
    val slot: Int
)