package nalgoticas.salle.cinetrack.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nalgoticas.salle.cinetrack.data.Movie
import nalgoticas.salle.cinetrack.data.remote.RatingUpdateRequest
import nalgoticas.salle.cinetrack.data.remote.RetrofitInstance

data class HomeUiState(
    val isLoading: Boolean = true,
    val movies: List<Movie> = emptyList(),
    val error: String? = null,
    val watchedIds: Set<Int> = emptySet(),
    val favoriteIds: Set<Int> = emptySet()
)

class HomeViewModel : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    private val api = RetrofitInstance.api

    init {
        loadMovies()
    }

    // Make this public so you can reload if needed
    fun loadMovies() {
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

    // Helper: find a movie by id from current state
    fun getMovieById(id: Int): Movie? =
        uiState.movies.find { it.id == id }

    // Update rating locally + in API
    fun updateMovieRating(movieId: Int, newRating: Float) {
        viewModelScope.launch {
            val previousMovies = uiState.movies

            // optimistic UI update
            val locallyUpdated = previousMovies.map { movie ->
                if (movie.id == movieId) movie.copy(rating = newRating)
                else movie
            }
            uiState = uiState.copy(movies = locallyUpdated)

            try {
                // call PUT /movies/{id} with body { "rating": newRating }
                val updatedMovie = api.updateRating(
                    id = movieId,
                    body = RatingUpdateRequest(rating = newRating)
                )

                // replace with server version
                val finalList = uiState.movies.map { movie ->
                    if (movie.id == movieId) updatedMovie else movie
                }
                uiState = uiState.copy(movies = finalList)

            } catch (e: Exception) {
                // rollback if error
                uiState = uiState.copy(
                    movies = previousMovies,
                    error = e.message ?: "Error al actualizar rating"
                )
            }
        }
    }

    fun toggleWatched(id: Int) {
        val current = uiState.watchedIds
        uiState = uiState.copy(
            watchedIds = if (id in current) current - id else current + id
        )
    }

    fun toggleFavorite(id: Int) {
        val current = uiState.favoriteIds
        uiState = uiState.copy(
            favoriteIds = if (id in current) current - id else current + id
        )
    }
}
