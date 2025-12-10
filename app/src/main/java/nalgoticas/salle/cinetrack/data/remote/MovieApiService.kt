package nalgoticas.salle.cinetrack.data.remote

import nalgoticas.salle.cinetrack.data.Movie
import nalgoticas.salle.cinetrack.ui.auth.Favorite
import nalgoticas.salle.cinetrack.ui.auth.FavoriteRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class RatingUpdateRequest(
    val rating: Float
)

interface MovieApiService {

    @GET("movies")
    suspend fun getMovies(): List<Movie>

    @GET("movies/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int
    ): Movie

    @PUT("movies/{id}")
    suspend fun updateRating(
        @Path("id") id: Int,
        @Body body: RatingUpdateRequest
    ): Movie

    @GET("favorites/user/{userId}")
    suspend fun getFavoritesByUser(
        @Path("userId") userId: Int
    ): List<Favorite>

    @POST("favorites")
    suspend fun addFavorite(
        @Body request: FavoriteRequest
    ): Favorite

    @DELETE("favorites/{favorite_id}")
    suspend fun deleteFavorite(
        @Path("favorite_id") id: Int

    )
}
