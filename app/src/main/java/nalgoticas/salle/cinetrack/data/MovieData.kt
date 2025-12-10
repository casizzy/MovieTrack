package nalgoticas.salle.cinetrack.data
import com.google.gson.annotations.SerializedName
data class Movie(
    val id: Int,
    val title: String,
    val year: Int,

    @SerializedName("duration_minutes")
    val durationMinutes: Int,
    @SerializedName("genre")
    val genre: String?,
    val rating: Float,

    @SerializedName("image_url")
    val imageUrl: String,
    val synopsis: String,
    val director: String,
    val cast: String
)

