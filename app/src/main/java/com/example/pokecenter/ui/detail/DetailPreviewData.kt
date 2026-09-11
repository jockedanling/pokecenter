package com.example.pokecenter.ui.detail

import com.example.pokecenter.domain.model.EvolutionChain
import com.example.pokecenter.domain.model.EvolutionStage
import com.example.pokecenter.domain.model.PokemonDetail
import com.example.pokecenter.domain.model.PokemonStat
import com.example.pokecenter.domain.model.PokemonType

// Testdata för DetailScreen — ersätts av ViewModel när allt kopplas ihop
object DetailPreviewData {
    val pokemon = PokemonDetail(
        id = 25,
        name = "pikachu",
        imageUrl = null,
        types = listOf(PokemonType.ELECTRIC),
        heightDecimeters = 4,
        weightHectograms = 60,
        baseExperience = 112,
        abilities = listOf("static", "lightning-rod"),
        stats = listOf(
            PokemonStat("hp", 35),
            PokemonStat("attack", 55),
            PokemonStat("defense", 40),
            PokemonStat("special-attack", 50),
            PokemonStat("special-defense", 50),
            PokemonStat("speed", 90)
        ),
        moves = listOf("thunder-shock", "quick-attack", "iron-tail", "thunderbolt")
    )

    val evolutionChain = EvolutionChain(
        stages = listOf(
            EvolutionStage(speciesId = 172, speciesName = "pichu", minLevel = null),
            EvolutionStage(speciesId = 25, speciesName = "pikachu", minLevel = null),
            EvolutionStage(speciesId = 26, speciesName = "raichu", minLevel = null)
        )
    )
}