package nalgoticas.salle.cinetrack.data
import com.google.gson.annotations.SerializedName
data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val durationMinutes: Int,
    val genres: List<String>,
    val rating: Float,

    @SerializedName("image_url")
    val imageUrl: String,
    val synopsis: String,
    val director: String,
    val cast: String
)

