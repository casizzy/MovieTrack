package nalgoticas.salle.cinetrack.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import nalgoticas.salle.cinetrack.data.Movie
import nalgoticas.salle.cinetrack.ui.screens.home.HomeViewModel
import nalgoticas.salle.cinetrack.ui.screens.home.components.MovieGrid

@Composable
fun DiscoverScreen(
    onMovieClick: (Movie) -> Unit,
    homeViewModel: HomeViewModel
) {
    val state = homeViewModel.uiState
    val bg = Color(0xFF050510)
    var query by remember { mutableStateOf("") }

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${state.error}",
                    color = Color.White
                )
            }
        }

        else -> {
            val filteredMovies = state.movies.filter { movie ->
                movie.title.contains(query, ignoreCase = true)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DiscoverTopBar()
                Spacer(Modifier.height(8.dp))
                DiscoverSearchField(
                    query = query,
                    onQueryChange = { query = it }
                )
                Spacer(Modifier.height(16.dp))
                MovieGrid(
                    movies = filteredMovies,
                    watchedIds = state.watchedIds,
                    favoriteIds = state.favoriteIds,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Composable
private fun DiscoverTopBar() {
    Text(
        text = "Discover",
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun DiscoverSearchField(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search films...",
                color = Color(0xFF8A8A99)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color(0xFF8A8A99)
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color(0xFFFF6B3D),
            unfocusedContainerColor = Color(0xFF12121E),
            focusedContainerColor = Color(0xFF12121E),
            cursorColor = Color.White
        )
    )
}
