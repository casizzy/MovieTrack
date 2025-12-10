package nalgoticas.salle.cinetrack.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nalgoticas.salle.cinetrack.data.Movie
import nalgoticas.salle.cinetrack.data.remote.RetrofitInstance

data class HomeUiState(
    val isLoading: Boolean = true,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    private val api = RetrofitInstance.api

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val movies = api.getMovies()
                uiState = uiState.copy(
                    isLoading = false,
                    movies = movies,
                    error = null
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = e.message ?: "Error cargando movies"
                )
            }
        }
    }
}
