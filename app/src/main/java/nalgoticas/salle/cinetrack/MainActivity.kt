package nalgoticas.salle.cinetrack

import nalgoticas.salle.cinetrack.ui.auth.LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import nalgoticas.salle.cinetrack.ui.components.CineTrackBottomBar
import nalgoticas.salle.cinetrack.ui.discover.DiscoverScreen
import nalgoticas.salle.cinetrack.ui.screens.home.diaryScreen.DiaryScreen
import nalgoticas.salle.cinetrack.ui.screens.home.HomeScreen
import nalgoticas.salle.cinetrack.ui.screens.home.MovieDetailScreen
import nalgoticas.salle.cinetrack.ui.screens.home.ProfileScreen
import nalgoticas.salle.cinetrack.ui.theme.CineTrackTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CineTrackTheme {
                CineTrackApp()
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun CineTrackPreview() {
    CineTrackTheme {
        CineTrackApp()
    }
}
