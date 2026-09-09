package com.example.pokecenter.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokecenter.data.repository.PokemonRepository
import com.example.pokecenter.domain.model.Pokemon
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: PokemonRepository
) : ViewModel() {
    val favorites: StateFlow<List<Pokemon>> = repository.getFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun removeFavorite(pokemonId: Int) {
        viewModelScope.launch { repository.removeFavorite(pokemonId) }
    }
}