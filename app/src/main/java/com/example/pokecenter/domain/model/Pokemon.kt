package com.example.pokecenter.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String?, // ? = kan vara null
    val types: List<PokemonType>
) {
    // Första typen avgör kortets bakgrundsfärg
    val primaryType: PokemonType
        get() = types.firstOrNull() ?: PokemonType.UNKNOWN

    val formattedNumber: String
        get() = "#${id.toString().padStart(3, '0')}"

    // Visar namn med stor bokstav
    val displayName: String
        get() = name.replaceFirstChar { it.uppercase()}


}