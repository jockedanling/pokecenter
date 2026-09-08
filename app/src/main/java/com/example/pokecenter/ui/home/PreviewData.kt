package com.example.pokecenter.ui.home

import com.example.pokecenter.domain.model.Pokemon
import com.example.pokecenter.domain.model.PokemonType

// Testdata för att bygga UI innan ViewModel finns
// Tas bort eller ersätts när de riktiga API-anropen kopplas in
object PreviewData {
    val pokemonList = listOf(
        Pokemon(1, "bulbasaur", null, listOf(PokemonType.GRASS, PokemonType.POISON)),
        Pokemon(4, "charmander", null, listOf(PokemonType.FIRE)),
        Pokemon(7, "squirtle", null, listOf(PokemonType.WATER)),
        Pokemon(25, "pikachu", null, listOf(PokemonType.ELECTRIC)),
        Pokemon(39, "jigglypuff", null, listOf(PokemonType.NORMAL, PokemonType.FAIRY)),
        Pokemon(94, "gengar", null, listOf(PokemonType.GHOST, PokemonType.POISON))
    )
}