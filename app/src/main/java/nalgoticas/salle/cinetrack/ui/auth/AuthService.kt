package nalgoticas.salle.cinetrack.ui.auth

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("users/login")
    suspend fun login(@Body login: Login): LoginResponse

    @POST("users/register")
    suspend fun register(@Body register: Register): RegisterResponse


}