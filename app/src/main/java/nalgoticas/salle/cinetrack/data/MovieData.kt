package nalgoticas.salle.cinetrack.data

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val durationMinutes: Int,
    val genres: List<String>,
    val rating: Float,
    val imageUrl: String,
    val synopsis: String,
    val director: String,
    val cast: String
)

