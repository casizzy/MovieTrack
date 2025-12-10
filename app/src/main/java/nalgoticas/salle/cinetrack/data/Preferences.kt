package nalgoticas.salle.cinetrack.data

import com.russhwolf.settings.Settings

object Preferences {
    private val settings = Settings()

    fun saveUserId(userId: Int){
        settings.putInt("userId", userId)
    }

    fun getUserId(): Int{
        return settings.getInt("userId", 0)
    }

    fun saveIsLogges(isLogged: Boolean){
        settings.putBoolean("isLogged", isLogged)
    }

    fun getIsLogged(): Boolean{
        return settings.getBoolean("isLogged", false)
    }

    fun clearSettinga(){
        settings.clear()
    }
}