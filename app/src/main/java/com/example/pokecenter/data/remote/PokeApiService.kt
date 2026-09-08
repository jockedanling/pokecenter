package com.example.pokecenter.data.remote

import com.example.pokecenter.data.remote.dto.EvolutionChainResponse
import com.example.pokecenter.data.remote.dto.PokemonDetailResponse
import com.example.pokecenter.data.remote.dto.PokemonListResponse
import com.example.pokecenter.data.remote.dto.PokemonSpeciesResponse
import com.example.pokecenter.data.remote.dto.TypeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailResponse

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): PokemonSpeciesResponse

    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(
        @Path("id") id: Int
    ): EvolutionChainResponse

    @GET("type/{id}")
    suspend fun getType(
        @Path("id") id: Int
    ): TypeResponse

}