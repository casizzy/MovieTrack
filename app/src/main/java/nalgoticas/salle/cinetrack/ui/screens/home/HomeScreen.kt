package nalgoticas.salle.cinetrack.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nalgoticas.salle.cinetrack.data.Movie
import nalgoticas.salle.cinetrack.ui.screens.home.components.MovieGrid

enum class MovieCategory(val label: String) {
    Trending("Trending"),
    Popular("Popular"),
    New("New")
}

@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit,
    viewModel: HomeViewModel
) {
    val state = viewModel.uiState
    val bg = Color(0xFF050510)
    var selectedCategory by remember { mutableStateOf(MovieCategory.Trending) }

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: ${state.error}")
            }
        }

        else -> {
            val moviesForCategory = when (selectedCategory) {
                MovieCategory.Trending ->
                    state.movies

                MovieCategory.Popular ->
                    state.movies.sortedByDescending { it.rating }

                MovieCategory.New ->
                    state.movies.sortedByDescending { it.id }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                CineTrackTopBar()
                Spacer(Modifier.height(24.dp))
                CategoryTabs(
                    selected = selectedCategory,
                    onSelectedChange = { selectedCategory = it }
                )
                Spacer(Modifier.height(16.dp))

                MovieGrid(
                    movies = moviesForCategory,
                    watchedIds = state.watchedIds,
                    favoriteIds = state.favoriteIds,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Composable
private fun CineTrackTopBar() {
    Text(
        text = "CineTrack",
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
    )
}

@Composable
private fun CategoryTabs(
    selected: MovieCategory,
    onSelectedChange: (MovieCategory) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xCC151521))
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MovieCategory.values().forEach { category ->
                val isSelected = category == selected

                val selectedBrush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF8A4D),
                        Color(0xFFFF4F6A)
                    )
                )

                val backgroundModifier = if (isSelected) {
                    Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(selectedBrush)
                } else {
                    Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Transparent)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .then(backgroundModifier)
                        .clickable { onSelectedChange(category) },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(
                            vertical = 6.dp,
                            horizontal = if (isSelected) 10.dp else 6.dp
                        )
                    ) {
                        if (category == MovieCategory.Trending) {
                            Icon(
                                imageVector = Icons.Filled.Whatshot,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else Color(0xFF8A8A99),
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 4.dp)
                            )
                        }

                        Text(
                            text = category.label,
                            color = if (isSelected) Color.White else Color(0xFF8A8A99),
                            fontSize = 13.sp,
                            fontWeight = if (isSelected)
                                androidx.compose.ui.text.font.FontWeight.SemiBold
                            else
                                androidx.compose.ui.text.font.FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
