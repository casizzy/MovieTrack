package nalgoticas.salle.cinetrack.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import nalgoticas.salle.cinetrack.data.Preferences
import nalgoticas.salle.cinetrack.ui.auth.AuthViewModel
import androidx.compose.runtime.LaunchedEffect

data class RecentActivity(
    val movieTitle: String,
    val time: String,
    val rating: Int
)

data class StatItem(
    val icon: ImageVector,
    val label: String,
    val count: String,
    val gradient: List<Color>
)

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    homeViewModel: HomeViewModel,
) {
    val authViewModel: AuthViewModel = viewModel()
    val bg = Color(0xFF050510)
    val userId = Preferences.getUserId()
    val name = authViewModel.getName(userId)

    val state = homeViewModel.uiState

    val watchedCount   = state.watchedIds.size
    val favoritesCount = state.favoriteIds.size
    val ratingsCount   = state.ratedMovieIds.size
    val reviewsCount   = state.reviewedMovieIds.size

    // Películas marcadas como watched
    val watchedMovies = state.movies.filter { it.id in state.watchedIds }

    // Lista de actividad reciente (últimas 5)
    val recentActivity = watchedMovies
        .takeLast(5)
        .map { movie ->
            RecentActivity(
                movieTitle = movie.title,
                time = "Recently",
                rating = movie.rating.toInt()
            )
        }

    LaunchedEffect(Unit) {
        homeViewModel.syncWithLoggedUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Profile",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(Modifier.height(16.dp))

        UserCard(
            name = name,
            username = "@${name}",
            onLogout = onLogout
        )

        Spacer(Modifier.height(24.dp))

        StatsGrid(
            watchedCount = watchedCount,
            ratingsCount = ratingsCount,
            reviewsCount = reviewsCount,
            favoritesCount = favoritesCount
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Recent Activity",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )

        Spacer(Modifier.height(14.dp))

        if (recentActivity.isEmpty()) {
            Text(
                text = "No recent activity yet",
                color = Color(0xFF8A8A99),
                fontSize = 13.sp
            )
        } else {
            recentActivity.forEach { activity ->
                RecentActivityCard(activity)
                Spacer(Modifier.height(14.dp))
            }
        }
    }
}

@Composable
private fun UserCard(
    name: String,
    username: String,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFF10101A))
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape)
                        .drawBehind {
                            drawRoundRect(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFF8A3C),
                                        Color(0xFFFF4F6A)
                                    )
                                ),
                                cornerRadius = CornerRadius(46.dp.toPx())
                            )
                        }
                        .padding(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFF2B2B38)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (name.isNotEmpty()) name.first().uppercase() else "A",
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = username,
                        fontSize = 14.sp,
                        color = Color(0xFFB0B0C0)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF11111C))
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFFF8A3C), Color(0xFFFF4F6A))
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { onLogout() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun StatsGrid(
    watchedCount: Int,
    ratingsCount: Int,
    reviewsCount: Int,
    favoritesCount: Int
) {
    val statsItems = listOf(
        StatItem(
            icon = Icons.Filled.Movie,
            label = "Films Watched",
            count = watchedCount.toString(),
            gradient = listOf(Color(0xFFFF8A3C), Color(0xFFFF4F6A))
        ),
        StatItem(
            icon = Icons.Filled.Star,
            label = "Ratings",
            count = ratingsCount.toString(),
            gradient = listOf(Color(0xFFFFC045), Color(0xFFFFA726))
        ),
        StatItem(
            icon = Icons.Filled.CalendarMonth,
            label = "Reviews",
            count = reviewsCount.toString(),
            gradient = listOf(Color(0xFF4FC3F7), Color(0xFF1976D2))
        ),
        StatItem(
            icon = Icons.Filled.Favorite,
            label = "Favorites",
            count = favoritesCount.toString(),
            gradient = listOf(Color(0xFFFF4F6A), Color(0xFFE91E63))
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                item = statsItems[0],
                modifier = Modifier.weight(1f)
            )
            StatCard(
                item = statsItems[1],
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                item = statsItems[2],
                modifier = Modifier.weight(1f)
            )
            StatCard(
                item = statsItems[3],
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    item: StatItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(130.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF050510))
            .border(
                width = 1.dp,
                color = Color(0x33FFFFFF),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF11111C))
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(item.gradient),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.gradient.last(),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = item.count,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = item.label,
                fontSize = 13.sp,
                color = Color(0xFF8A8A99)
            )
        }
    }
}

@Composable
private fun RecentActivityCard(activity: RecentActivity) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF10101A))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF11111C))
                        .border(
                            width = 1.5.dp,
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFF4FC3F7),
                                    Color(0xFF1976D2)
                                )
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC045),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Reviewed ",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = activity.movieTitle,
                            fontSize = 14.sp,
                            color = Color(0xFFFF8A3C),
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = activity.time,
                            fontSize = 12.sp,
                            color = Color(0xFF8A8A99)
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Row {
                repeat(activity.rating) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC045),
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}
