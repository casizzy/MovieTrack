package nalgoticas.salle.cinetrack.data.remote

import nalgoticas.salle.cinetrack.data.Movie
import retrofit2.http.Body
import retrofit2.http.GET
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
}
