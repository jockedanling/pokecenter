package com.example.pokecenter.di

import android.content.Context
import androidx.room.Room
import com.example.pokecenter.data.local.FavoriteDao
import com.example.pokecenter.data.local.PokemonDatabase
import com.example.pokecenter.data.remote.PokeApiService
import com.example.pokecenter.data.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://pokeapi.co/api/v2/"
@Module
@InstallIn(SingletonComponent::class)

class AppModule {
    // Nätverk

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
        /* -- Detta kräver en till dependency om vi vill ha loggning.
        .addInterceptor(
            HttpLoggingInterceptor().apply { level =
        HttpLoggingInterceptor.Level.BASIC }
        )
        */
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json):
            Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun providePokeApiService(retrofit: Retrofit):
            PokeApiService =
        retrofit.create(PokeApiService::class.java)

    // Databasen

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext
    context: Context
    ): PokemonDatabase =
        Room.databaseBuilder(context, PokemonDatabase::class.java, "pokemon.db").build()

    @Provides
    fun provideFavoriteDao(database: PokemonDatabase):
            FavoriteDao = database.favoriteDao()

    // Repository

    @Provides
    @Singleton
    fun providePokemonRepository(
        api: PokeApiService,
        favoriteDao: FavoriteDao):
            PokemonRepository = PokemonRepository(api, favoriteDao
    )
}