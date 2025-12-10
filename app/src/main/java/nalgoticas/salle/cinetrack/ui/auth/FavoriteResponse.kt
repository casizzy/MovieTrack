package nalgoticas.salle.cinetrack.ui.auth

import com.google.gson.annotations.SerializedName

data class FavoriteRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("movie_id") val movieId: Int
)
