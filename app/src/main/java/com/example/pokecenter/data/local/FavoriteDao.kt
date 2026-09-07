package com.example.pokecenter.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

// Returnerar en Flow och uppdaterar automatiskt tabellen vid förändring
@Dao
interface FavoriteDao{
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

// En boolean som praktiskt reagerar live om något togglas.
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE pokemonId = :pokemonId)")
    fun isFavorite(pokemonId: Int): Flow<Boolean>

// En insert med REPLACE som hanterar dubletter. Sparar men skriver över raden.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

// Delete tar bort Id:t och inte hela entiteten.
    @Query("DELETE FROM favorites WHERE pokemonId = :pokemonId")
    suspend fun delete(pokemonId: Int)
}