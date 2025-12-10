package nalgoticas.salle.cinetrack.ui.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val user_id: Int,
    val username: String,
    val email: String
)
