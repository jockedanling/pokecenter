package com.example.pokecenter.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @kotlin.jvm.Volatile
        private var INSTANCE: PokemonDatabase? = null

        // Eventuellt överflödig denna funktion när Room byggs via Hilt.
        fun getInstance(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, PokemonDatabase::class.java, "pokemon.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}