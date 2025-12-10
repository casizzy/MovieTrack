package nalgoticas.salle.cinetrack.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Whatshot
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
import coil.compose.AsyncImage
import nalgoticas.salle.cinetrack.data.Movie
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import nalgoticas.salle.cinetrack.ui.screens.home.components.MovieCard

enum class MovieCategory(val label: String) {
    Trending("Trending"),
    Popular("Popular"),
    New("New")
}

@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit,
    viewModel: HomeViewModel = viewModel()
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                CineTrackTopBar()
                Spacer(Modifier.height(8.dp))
                SearchField()
                Spacer(Modifier.height(16.dp))
                CategoryTabs(
                    selected = selectedCategory,
                    onSelectedChange = { selectedCategory = it }
                )
                Spacer(Modifier.height(16.dp))

                MovieGrid(
                    movies = state.movies,
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
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SearchField() {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
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
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieGrid(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies, key = { it.id }) { movie ->
            MovieCard(
                movie = movie,
                onClick = { onMovieClick(movie) }
            )
        }
    }
}

