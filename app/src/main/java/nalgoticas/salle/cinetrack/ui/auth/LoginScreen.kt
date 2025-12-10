package nalgoticas.salle.cinetrack.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import nalgoticas.salle.cinetrack.data.Preferences
import nalgoticas.salle.cinetrack.R

@Composable
fun LoginScreen(
    nav: NavController,
    onSignUpClick: () -> Unit = {}
) {
    val vm: AuthViewModel = viewModel()
    var passwordVisible by remember { mutableStateOf(false) }

    var visible by remember { mutableStateOf(false) }
    val isLogged = Preferences.getIsLogged()

    LaunchedEffect(isLogged) {
        if (isLogged){
            nav.navigate("home"){
                popUpTo("login"){
                    inclusive = true
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF050609),
                        Color(0xFF000000)
                    )
                )
            )
            .padding(24.dp)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(600)) +
                    slideInVertically(animationSpec = tween(600)) { it / 4 },
            exit = fadeOut(animationSpec = tween(300)) +
                    slideOutVertically(animationSpec = tween(300)) { it / 4 },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cinetracklogo),
                    contentDescription = "CineTrack logo",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "MovieTrack",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD1A0)
                )

                Text(
                    text = "Track, rate, and discover films",
                    fontSize = 14.sp,
                    color = Color(0xFFB3B3B3),
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 24.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0x1AFFFFFF),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Welcome back",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Email",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = vm.email,
                        onValueChange = { vm.email = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Enter your email",
                                color = Color(0xFF8A8A8A)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Password",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = vm.password,
                        onValueChange = { vm.password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            Text(
                                text = if (passwordVisible) "Hide" else "Show",
                                color = Color(0xFFB3B3B3),
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .clickable { passwordVisible = !passwordVisible }
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Enter your password",
                                color = Color(0xFF8A8A8A)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (vm.email.isBlank() || vm.password.isBlank()){
                                return@Button
                            } else {
                                vm.login(
                                    vm.email,
                                    vm.password
                                )
                                nav.navigate("home"){
                                    popUpTo("login"){
                                        inclusive = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFFF8A3D),
                                            Color(0xFFFF4F8B)
                                        )
                                    ),
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sign In",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Don't have an account? ",
                            color = Color(0xFFB3B3B3),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Sign up",
                            color = Color(0xFFFF8A3D),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                nav.navigate("signup")
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your personal film diary",
                    color = Color(0xFF808080),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    MaterialTheme {
        Box(modifier = Modifier.background(Color.Black)) {
            LoginScreen(rememberNavController())
        }
    }
}
