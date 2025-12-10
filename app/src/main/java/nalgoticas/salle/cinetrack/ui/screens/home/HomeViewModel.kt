package nalgoticas.salle.cinetrack.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import nalgoticas.salle.cinetrack.data.Movie
import nalgoticas.salle.cinetrack.data.Preferences
import nalgoticas.salle.cinetrack.data.ReviewRequest
import nalgoticas.salle.cinetrack.ui.auth.Favorite
import nalgoticas.salle.cinetrack.data.remote.MovieApiService
import nalgoticas.salle.cinetrack.data.remote.RatingUpdateRequest
import nalgoticas.salle.cinetrack.data.remote.RetrofitInstance
import nalgoticas.salle.cinetrack.data.remote.RetrofitInstance.api
import nalgoticas.salle.cinetrack.ui.auth.FavoriteRequest

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


    private var currentUserId: Int? = null


    fun syncWithLoggedUser() {
        val storedUserId = Preferences.getUserId()
        if (storedUserId == null) return
        if (storedUserId == currentUserId) return
        currentUserId = storedUserId
        loadMoviesAndFavorites(storedUserId)
    }

    fun sendReview(
        movieId: Int,
        reviewText: String
    ) {
        viewModelScope.launch {
            val userId = Preferences.getUserId()
            if (userId == 0) {
                uiState = uiState.copy(error = "No user id stored")
                return@launch
            }

            try {
                val api = RetrofitInstance.api.create(MovieApiService::class.java)

                api.postReview(
                    ReviewRequest(
                        content = reviewText,
                        userId = userId,
                        movieId = movieId
                    )
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.message ?: "Error enviando review"
                )
            }
        }
    }






    private fun loadMoviesAndFavorites(userId: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            try {
                val service = api.create(MovieApiService::class.java)

                // películas
                val moviesDeferred = async { service.getMovies() }
                val movies = moviesDeferred.await()

                //favoritos del usuario
                val favoritesDeferred = async { service.getFavoritesByUser(userId) }
                val favorites: List<Favorite> = favoritesDeferred.await()
                val favoriteIds = favorites.map { it.movieId }.toSet()

                uiState = uiState.copy(
                    isLoading = false,
                    movies = movies,
                    favoriteIds = favoriteIds,
                    error = null
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = e.message ?: "Error cargando datos"
                )
            }
        }
    }















    fun getMovieById(id: Int): Movie? =
        uiState.movies.find { it.id == id }

    fun updateMovieRating(movieId: Int, newRating: Float) {
        viewModelScope.launch {
            val previousMovies = uiState.movies


            val locallyUpdated = previousMovies.map { movie ->
                if (movie.id == movieId) movie.copy(rating = newRating)
                else movie
            }
            uiState = uiState.copy(movies = locallyUpdated)

            try {

                val api = api.create(MovieApiService::class.java)
                val updatedMovie = api.updateRating(
                    id = movieId,
                    body = RatingUpdateRequest(rating = newRating)
                )


                val finalList = uiState.movies.map { movie ->
                    if (movie.id == movieId) updatedMovie else movie
                }
                uiState = uiState.copy(movies = finalList)

            } catch (e: Exception) {

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

    fun toggleFavorite(movieId: Int) {


        val userId = currentUserId ?: return
        val wasFavorite = movieId in uiState.favoriteIds
        val previousFavorites = uiState.favoriteIds
        val newFavorites = previousFavorites.toMutableSet().apply{
            if (wasFavorite) remove(movieId) else add(movieId)
        }



        uiState = uiState.copy(favoriteIds = newFavorites)




        viewModelScope.launch {
            try {
                val service = api.create(MovieApiService::class.java)

                if (wasFavorite) {
                    val favoritesFromApi = service.getFavoritesByUser(userId)
                    val favoriteToDelete = favoritesFromApi.firstOrNull { it.movieId == movieId }

                    if (favoriteToDelete != null) {
                        service.deleteFavorite(favoriteToDelete.id)
                    }
                } else {

                    val request = FavoriteRequest(
                        userId = userId,
                        movieId = movieId
                    )
                    service.addFavorite(request)
                }

            } catch (e: Exception) {

                uiState = uiState.copy(
                    favoriteIds = previousFavorites,
                    error = e.message ?: "Error updating favorites"
                )
            }
        }
    }

}
