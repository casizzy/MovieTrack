package nalgoticas.salle.cinetrack.ui.auth

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
    val email: String
)
