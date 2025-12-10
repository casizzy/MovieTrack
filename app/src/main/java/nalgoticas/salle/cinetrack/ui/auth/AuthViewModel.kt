package nalgoticas.salle.cinetrack.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import nalgoticas.salle.cinetrack.data.Preferences
import nalgoticas.salle.cinetrack.data.remote.RetrofitInstance.api

class AuthViewModel: ViewModel() {

    var name by mutableStateOf("")
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isLogged by mutableStateOf(false)

    fun register(
        username: String,
        email: String,
        password: String
    ){
        viewModelScope.launch {
            try {
                val service = api.create(AuthService::class.java)
                val register = Register(
                    username,
                    email,
                    password
                )
                val result  = async {
                    service.register(register)
                }
                var user = result.await()
                isLogged = true
                Preferences.saveUserId(user.id)
                Preferences.saveIsLogges(isLogged)
            } catch (e: Error){
                isLogged = false
                println("Error: No se registró ningun usuario")
                println(e.toString())

            }
        }
    }

    fun login(
        email:String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                val service = api.create(AuthService::class.java)
                val login = Login(
                    email,
                    password
                )
                val result = async {
                    service.login(login)
                }
                val response = result.await()
                if (response.message == "Login exitoso"){
                    isLogged = true
                    Preferences.saveUserId(response.user_id)
                    Preferences.saveIsLogges(isLogged)
                }

            } catch (e: Error){
                isLogged = false
                println("Error: No inicio sesión")
                println(e.toString())
            }
        }
    }



}