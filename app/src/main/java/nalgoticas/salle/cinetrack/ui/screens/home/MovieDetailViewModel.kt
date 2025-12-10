package nalgoticas.salle.cinetrack.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nalgoticas.salle.cinetrack.data.Movie
import nalgoticas.salle.cinetrack.data.remote.RetrofitInstance

data class MovieDetailUiState(
    val isLoading: Boolean = true,
    val movie: Movie? = null,
    val error: String? = null
)

class MovieDetailViewModel(
    private val movieId: Int
) : ViewModel() {

    var uiState by mutableStateOf(MovieDetailUiState())
        private set

    private val api = RetrofitInstance.api

    init {
        loadMovie()
    }

    private fun loadMovie() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val movie = api.getMovieById(movieId)
                uiState = uiState.copy(
                    isLoading = false,
                    movie = movie,
                    error = null
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = e.message ?: "Error loading movie"
                )
            }
        }
    }
}
