package com.example.pokecenter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

// Room-databasen. Instansen byggs av hilt i AppModule, inte här
@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}