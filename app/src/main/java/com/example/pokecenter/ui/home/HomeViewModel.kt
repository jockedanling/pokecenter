package com.example.pokecenter.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokecenter.data.repository.PokemonRepository
import com.example.pokecenter.domain.model.Pokemon
import com.example.pokecenter.domain.model.PokemonType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val pokemonList: List<Pokemon> = emptyList(),
    val searchQuery: String = "",
    val selectedType: PokemonType? = null,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val endReached: Boolean = false
)
class HomeViewModel(
    private val repository: PokemonRepository ) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val allLoaded = mutableListOf<Pokemon>() // ofiltrerad master-listan
    private var offset = 0
    private val pageSize = 20

    init { loadNextPage() }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query)}
        applyFilter()
    }
    fun onTypeSelected(type: PokemonType?) {
        _uiState.update {
            it.copy(
                selectedType = if (
                    it.selectedType == type) null else type)
        }
        applyFilter()
    }
        fun loadNextPage() {
            val s = _uiState.value
            if (s.isLoading || s.isLoadingMore || s.endReached) return

            viewModelScope.launch { _uiState.update { if (offset == 0)
            it.copy(isLoading = true) else it.copy(isLoadingMore = true)}
            try {
                val page = repository.getPokemonList(limit = pageSize, offset = offset)
                allLoaded += page
                offset += pageSize
                _uiState.update {
                    it.copy(isLoading = false, isLoadingMore = false, endReached = page.isEmpty())
                }

                applyFilter()
            } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, isLoadingMore = false, error = e.message)}
                }
            }
        }

                private fun applyFilter() {
                val s = _uiState.value
                val filtered = allLoaded.filter { p -> p.name.contains(s.searchQuery, ignoreCase = true) &&
                        (s.selectedType == null || p.types.contains(s.selectedType))
                }
                _uiState.update { it.copy(pokemonList = filtered)
                }
                }
            }


