package com.example.pokecenter.ui.detail

 import androidx.lifecycle.SavedStateHandle
 import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokecenter.data.repository.PokemonRepository
import com.example.pokecenter.domain.model.EvolutionChain
import com.example.pokecenter.domain.model.PokemonDetail
 import dagger.hilt.android.lifecycle.HiltViewModel
 import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
 import javax.inject.Inject


data class DetailUiState(
    val pokemon: PokemonDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val evolutionChain: EvolutionChain? = null,
    val isEvolutionLoading: Boolean = false,
    val evolutionError: String? = null
)
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle ) : ViewModel() {
    private val pokemonId: Int = checkNotNull(savedStateHandle["pokemonId"])

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init { loadPokemon() }
    private fun loadPokemon() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val detail = repository.getPokemonDetail(pokemonId)
                _uiState.update { it.copy(pokemon = detail, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }


        // Anropas av UI först när användaren trycker på Evolution-tabben
        fun loadEvolutionChain() {
            val s = _uiState.value
            if (s.evolutionChain != null || s.isEvolutionLoading) return

            viewModelScope.launch {
                _uiState.update { it.copy(isEvolutionLoading = true, evolutionError = null) }
                try {
                    val chain = repository.getEvolutionChain(pokemonId)
                    _uiState.update { it.copy(evolutionChain = chain, isEvolutionLoading = false) }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            isEvolutionLoading = false,
                            evolutionError = e.message) }
                }
            }
        }
    }


