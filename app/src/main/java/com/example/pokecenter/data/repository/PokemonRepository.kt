package com.example.pokecenter.data.repository

import com.example.pokecenter.data.local.FavoriteDao
import com.example.pokecenter.data.local.FavoriteEntity
import com.example.pokecenter.data.remote.PokeApiService
import com.example.pokecenter.data.remote.dto.ChainLinkDto
import com.example.pokecenter.data.remote.dto.EvolutionChainResponse
import com.example.pokecenter.data.remote.dto.PokemonDetailResponse
import com.example.pokecenter.data.remote.dto.PokemonListItemDto
import com.example.pokecenter.domain.model.EvolutionChain
import com.example.pokecenter.domain.model.EvolutionStage
import com.example.pokecenter.domain.model.Pokemon
import com.example.pokecenter.domain.model.PokemonDetail
import com.example.pokecenter.domain.model.PokemonStat
import com.example.pokecenter.domain.model.PokemonType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.ConcurrentHashMap

class PokemonRepository (
    private val api: PokeApiService, // Konstruktorn
    private val favoriteDao: FavoriteDao
) {

    // Cachar rådata per Pokémon-id så samma Pokémon aldrig hämtas två gånger från nätet
    private val detailCache = ConcurrentHashMap<Int,
            PokemonDetailResponse>()
    private suspend fun fetchDetail(id: Int):
            PokemonDetailResponse = detailCache[id] ?: api.getPokemonDetail(id).also { detailCache[id] = it }
    suspend fun getPokemonList(limit: Int, offset: Int):
            List<Pokemon> = coroutineScope { val listResponse = api.getPokemonList(limit, offset)

        // Listan har bara namn + url, som hämtar detalj per pokemon parallellt för types/bild
        listResponse.results
            .map { item -> async {
                fetchDetail(item.extractId()) }} // Plockar ut ID ur URL:en.
            .awaitAll()
            .map { it.toPokemon() }
            }
    suspend fun getPokemonDetail(id: Int): PokemonDetail {
        return fetchDetail(id).toPokemonDetail()
    }
    suspend fun getEvolutionChain(speciesId: Int):
            EvolutionChain {
        val species = api.getPokemonSpecies(speciesId)
        val chainId = species.evolutionChain.url.trimEnd('/').substringAfterLast('/').toInt()
        return api.getEvolutionChain(chainId).toEvolutionChain()
    }
    fun getFavorites(): Flow<List<Pokemon>> =
        favoriteDao.getAllFavorites().map { entities -> entities.map { it.toPokemon()}}

    suspend fun addFavorite(pokemon: Pokemon) {
        favoriteDao.insert(
            FavoriteEntity(
                pokemonId = pokemon.id,
                name = pokemon.name,
                spriteUrl = pokemon.imageUrl,
                primaryType = pokemon.primaryType.name
            )
        )
    }
    suspend fun removeFavorite(pokemonId: Int) {
        favoriteDao.delete(pokemonId)
    }
// En funktion för att kunna toogla favorit pokemon i detaljvyn sedan.
    fun isFavorite(pokemonId: Int): Flow<Boolean> =
        favoriteDao.isFavorite(pokemonId)
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

private fun EvolutionChainResponse.toEvolutionChain():
        EvolutionChain = EvolutionChain(stages = chain.toStages())
private fun ChainLinkDto.toStages(): List<EvolutionStage> {
    val id = species.url.trimEnd('/').substringAfterLast('/').toInt()
    val stage = EvolutionStage(
        speciesId = id,
        speciesName = species.name,
        minLevel = evolutionDetails.firstOrNull()?.minlevel
    )
    val next = evolvesTo.firstOrNull()?.toStages() ?:
    emptyList()
    return listOf(stage) + next
}
private fun FavoriteEntity.toPokemon(): Pokemon = Pokemon(
    id = pokemonId,
    name = name,
    imageUrl = spriteUrl,
    types = listOf(PokemonType.fromApiName(primaryType))
)