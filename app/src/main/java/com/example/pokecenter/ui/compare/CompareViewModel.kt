package com.example.pokecenter.ui.compare


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokecenter.data.repository.PokemonRepository
import com.example.pokecenter.domain.model.PokemonDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Ett stat-par (t.ex. HP för pokemon A vs B) som CompareScreen kan visa sida vid sida
data class StatComparison(
    val statLabel: String,
    val valueA: Int,
    val valueB: Int
) {
    // Används av UI för att highlighta vilken sida som "vinner" i dessa stats
    val aIsHigher: Boolean get() = valueA > valueB
    val bIsHigher: Boolean get() = valueB > valueA
}

data class CompareUiState(
    val firstPokemon: PokemonDetail? = null,
    val secondPokemon: PokemonDetail? = null,
    val isLoadingFirst: Boolean = false,
    val isLoadingSecond: Boolean = false,
    val error: String? = null
) {
    // Räknas om automatiskt varje gång firstPokemon/secondPokemon ändras
    val statComparison: List<StatComparison>
        get() {
            // Tom lista tills båda är valda.
            // UI visar bara jämförelse när det finns något att jämföra
            val a = firstPokemon ?: return emptyList()
            val b = secondPokemon ?: return emptyList()
            // Zip parar ihop stats i samma ordning från båda listorna
            return a.stats.zip(b.stats) {statA, statB ->
                StatComparison(statA.shortLabel,statA.value, statB.value)
            }
        }
}
class CompareViewModel (
    private val repository: PokemonRepository ): ViewModel() {
    // Ett par där Viewmodelen får bara ändra state och UI får bara läsa
    private val _uiState = MutableStateFlow(CompareUiState())
    val uiState: StateFlow<CompareUiState> = _uiState.asStateFlow()

    fun selectFirst(pokemonId: Int) =
        selectSlot(pokemonId, isFirst = true)

    fun selectSecond(pokemonId: Int) =
        selectSlot(pokemonId, isFirst = false)

    // Hämtar detaljer för vald pokemon och lägger den i rätt sida av jämförelsen
    private fun selectSlot(pokemonId: Int, isFirst: Boolean) {
        viewModelScope.launch { // Sätt loading på rätt sida utan att röra den andra sidans state
            _uiState.update {
                if (isFirst) it.copy(isLoadingFirst = true, error = null)
                else it.copy(isLoadingSecond = true, error = null)
            }
            try {
                val detail = repository.getPokemonDetail(pokemonId)
                _uiState.update {
                    if (isFirst) it.copy(firstPokemon = detail, isLoadingFirst = false)
                    else it.copy(secondPokemon = detail, isLoadingSecond = false)
                }
            }
            catch(e: Exception) {
                    _uiState.update {
                        if (isFirst) it.copy(isLoadingFirst = false, error = e.message)
                        else it.copy(isLoadingSecond = false, error = e.message)
                    }
                }
            }
        }
    }