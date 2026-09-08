package com.example.pokecenter.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// toppnivå-objekt. Matchar hella Json-svaret.
@Serializable
data class PokemonDetailResponse (
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    @SerialName("base_experience") val baseExperience: Int? = null,
    val types: List<PokemonTypeSlotDto>,
    val abilities: List<PokemonAbilitySlotDto>,
    val stats: List<PokemonStatsDto>,
    val sprites: PokemonSpritesDto,
    val moves: List<PokemonMoveSlotDto>
)

@Serializable
data class PokemonTypeSlotDto (
    val slot: Int,
    val type: NamedApiResourceDto
)
@Serializable
data class PokemonAbilitySlotDto (
    val ability: NamedApiResourceDto,
    @SerialName("is_hidden") val isHidden: Boolean = false
)

@Serializable
data class PokemonStatsDto (
    @SerialName("base_stat") val baseStat: Int,
    val stat: NamedApiResourceDto
)
@Serializable
data class PokemonMoveSlotDto (
    val move: NamedApiResourceDto
)
@Serializable
data class NamedApiResourceDto (
    val name: String,
    val url: String
)
@Serializable
data class PokemonSpritesDto(
    @SerialName("front_default") val frontDefault: String? = null,
    @SerialName("front_shiny") val frontShiny: String? = null,
    val other: OtherSpritesDto? = null
)
@Serializable
data class OtherSpritesDto(
    @SerialName("official-artwork") val officialArtwork: OfficialArtworkDto? = null,
    @SerialName("dream_world") val dreamWorld: DreamWorldDto? = null
)
@Serializable
data class OfficialArtworkDto (
    @SerialName("front_default") val frontDefault: String? = null
)
@Serializable
data class DreamWorldDto (
    @SerialName("front_default") val frontDefault: String? = null
)
