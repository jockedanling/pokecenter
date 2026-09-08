package com.example.pokecenter.domain.model

import androidx.compose.ui.graphics.Color

enum class PokemonType(
    val displayName: String,
    val cardBackground: Color,
    val badgeColor: Color
) {
    GRASS("Grass", Color(0xFFC6EFCE), Color(0xFF7AC74C)),
    FIRE("Fire", Color(0xFFFDDCB5), Color(0xFFEE8130)),
    WATER("Water", Color(0xFFBDE0FE), Color(0xFF6390F0)),
    ELECTRIC("Electric", Color(0xFFFFF5B5), Color(0xFFF7D02C)),
    POISON("Poison", Color(0xFFE8D5F5), Color(0xFFA98CDB)),
    GHOST("Ghost", Color(0xFFE8D5F5), Color(0xFF735797)),
    FLYING("Flying", Color(0xFFD5D5F5), Color(0xFFA890F0)),
    BUG("Bug", Color(0xFFD5E8A0), Color(0xFFA6B91A)),
    NORMAL("Normal", Color(0xFFE8E8E0), Color(0xFFA8A878)),
    FIGHTING("Fighting", Color(0xFFF5C6B5), Color(0xFFC22E28)),
    PSYCHIC("Psychic", Color(0xFFF5C6D5), Color(0xFFF95587)),
    ROCK("Rock", Color(0xFFE8DCC8), Color(0xFFB6A136)),
    GROUND("Ground", Color(0xFFF5E0B5), Color(0xFFE2BF65)),
    ICE("Ice", Color(0xFFC6F0F0), Color(0xFF96D9D6)),
    DRAGON("Dragon", Color(0xFFC6C6F5), Color(0xFF6F35FC)),
    DARK("Dark", Color(0xFFC8C0B8), Color(0xFF705746)),
    STEEL("Steel", Color(0xFFD8D8E0), Color(0xFFB7B7CE)),
    FAIRY("Fairy", Color(0xFFF5C6D8), Color(0xFFD685AD)),
    UNKNOWN("Unknown", Color(0xFFE0E0E0), Color(0xFF999999));

    companion object {
        fun fromApiName(name: String): PokemonType {
            return entries.find { it.name.equals(name, ignoreCase = true) } ?: UNKNOWN
        }
    }
}