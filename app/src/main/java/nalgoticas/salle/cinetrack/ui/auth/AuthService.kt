package nalgoticas.salle.cinetrack.ui.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthService {

    @POST("users/login")
    suspend fun login(@Body login: Login): LoginResponse

    @POST("users/register")
    suspend fun register(@Body register: Register): RegisterResponse

    @GET("/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): User

}