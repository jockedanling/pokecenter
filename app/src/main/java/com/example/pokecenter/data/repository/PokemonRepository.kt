package com.example.pokecenter.data.repository

import com.example.pokecenter.data.remote.PokeApiService
import com.example.pokecenter.data.remote.dto.PokemonDetailResponse
import com.example.pokecenter.data.remote.dto.PokemonListItemDto
import com.example.pokecenter.domain.model.Pokemon
import com.example.pokecenter.domain.model.PokemonDetail
import com.example.pokecenter.domain.model.PokemonStat
import com.example.pokecenter.domain.model.PokemonType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PokemonRepository (
    private val api: PokeApiService // Konstruktorn
) {
    suspend fun getPokemonList(limit: Int, offset: Int):
            List<Pokemon> = coroutineScope { val listResponse = api.getPokemonList(limit, offset)

        // Listan har bara namn + url, som hämtar detalj per pokemon parallellt för types/bild
        listResponse.results
            .map { item -> async {
                api.getPokemonDetail(item.extractId()) }} // Plockar ut ID ur URL:en.
            .awaitAll()
            .map { it.toPokemon() }
            }
    suspend fun getPokemonDetail(id: Int): PokemonDetail {
        return api.getPokemonDetail(id).toPokemonDetail()
    }
}

// Mapping av DTO -> domain
private fun PokemonListItemDto.extractId(): Int =
    url.trimEnd('/').substringAfterLast('/').toInt()

private fun PokemonDetailResponse.toPokemon(): Pokemon =
    Pokemon(
        id = id,
        name = name,
        imageUrl = sprites.other?.officialArtwork?.frontDefault ?:
        sprites.frontDefault,
        types = types.sortedBy { it.slot }.map { // Ser till att säkerställa att primärtypen alltid hamnar först även om API:et returnerar i annan ordning.
            PokemonType.fromApiName(it.type.name)
        }
    )

private fun PokemonDetailResponse.toPokemonDetail():
        PokemonDetail = PokemonDetail (
            id = id,
            name = name,
            imageUrl =
                sprites.other?.officialArtwork?.frontDefault ?:
                sprites.frontDefault,
            types = types.sortedBy { it.slot }.map {
                PokemonType.fromApiName(it.type.name)
            },
            heightDecimeters = height,
            weightHectograms = weight,
            baseExperience = baseExperience,
            abilities = abilities.map {it.ability.name},
            stats = stats.map {
                PokemonStat(
                    name = it.stat.name,
                    value = it.baseStat
                )
            },
            moves = moves.map { it.move.name }
        )