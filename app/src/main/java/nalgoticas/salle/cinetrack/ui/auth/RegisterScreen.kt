package nalgoticas.salle.cinetrack.ui.auth

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import nalgoticas.salle.cinetrack.R


@Composable
fun RegisterScreen(
    nav: NavController,
) {
    val vm: AuthViewModel = viewModel()
    var errorText by remember { mutableStateOf<String?>(null) }

    val orange = Color(0xFFFF7A1A)
    val pink = Color(0xFFFF2F92)


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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Simple CT logo
            CineTrackHeader()

            Spacer(modifier = Modifier.height(24.dp))

            // Card with form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x33FFFFFF) // glassmorphism feel
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = "Create Account",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    if (errorText != null) {
                        Text(
                            text = errorText!!,
                            color = Color(0xFFFF7373),
                            fontSize = 13.sp
                        )
                    }

                    CineTrackTextField(
                        label = "Full Name",
                        value = vm.name,
                        onValueChange = { vm.name = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF9CA3AF)
                            )
                        }
                    )

                    CineTrackTextField(
                        label = "Email",
                        value = vm.email,
                        onValueChange = { vm.email = it },
                        keyboardType = KeyboardType.Email,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = Color(0xFF9CA3AF)
                            )
                        }
                    )

                    CineTrackTextField(
                        label = "Username",
                        value = vm.username,
                        onValueChange = { vm.username = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF9CA3AF)
                            )
                        }
                    )

                    CineTrackTextField(
                        label = "Password",
                        value = vm.password,
                        onValueChange = { vm.password = it },
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF9CA3AF)
                            )
                        }
                    )

                    CineTrackTextField(
                        label = "Confirm Password",
                        value = vm.confirmPassword,
                        onValueChange = { vm.confirmPassword = it },
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF9CA3AF)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            when {
                                vm.name.isBlank() || vm.email.isBlank() ||
                                        vm.username.isBlank() || vm.password.isBlank() ||
                                        vm.confirmPassword.isBlank() -> {
                                    errorText = "Please fill in all fields"
                                }

                                vm.password != vm.confirmPassword -> {
                                    errorText = "Passwords do not match"
                                }

                                else -> {
                                    errorText = null
                                    vm.register(
                                        username = vm.username,
                                        email= vm.email,
                                        password = vm.password
                                    )
                                    nav.navigate("home"){
                                        popUpTo("signup"){
                                            inclusive = true
                                        }
                                    }

                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(listOf(orange, pink)),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Create account",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            color = Color(0xFF9CA3AF),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Sign in",
                            color = orange,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                nav.popBackStack()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Join thousands of film lovers",
                color = Color(0xFF6B7280),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
private fun CineTrackHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Image(
                painter = painterResource(id = R.drawable.cinetracklogo),
                contentDescription = "CineTrack logo",
                modifier = Modifier.size(120.dp)
            )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CineTrackTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        leadingIcon = leadingIcon,
        visualTransformation = if (isPassword) PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0x33000000),
            unfocusedContainerColor = Color(0x33000000),
            focusedBorderColor = Color(0x33FFFFFF),
            unfocusedBorderColor = Color(0x22FFFFFF),
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color(0xFF9CA3AF),
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(18.dp)
    )
}

@Preview(
    name = "Register – Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RegisterScreenPreview() {
    MaterialTheme {
        RegisterScreen(rememberNavController())
    }
}
