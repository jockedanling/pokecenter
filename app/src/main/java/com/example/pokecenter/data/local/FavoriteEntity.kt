package com.example.pokecenter.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val pokemonId: Int,
    val name: String,
    val spriteUrl: String?,
    val primaryType: String
)