package com.example.contexta.auth.signup.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contexta.R
import com.example.contexta.auth.state.AuthState
import com.example.contexta.ui.components.AppButton
import com.example.contexta.ui.components.AuthPasswordField
import com.example.contexta.ui.components.AuthTextField
import com.example.contexta.ui.theme.ContextaTheme
import com.example.contexta.ui.theme.googleColors
import com.example.contexta.ui.theme.manropeFontFamily
import com.example.contexta.ui.theme.robotoFontFamily

@Composable
fun SignUpScreen(
    authState: AuthState,
    onSignUp: (email: String, password: String, fullName: String) -> Unit,
    onSignUpWithGoogle: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val colors = MaterialTheme.googleColors

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading = authState is AuthState.Loading

    Scaffold { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            //  Top Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_foreground),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(100.dp)
                )

                Text(
                    text = "Contexta",
                    fontFamily = manropeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 26.sp
                )
            }

            //  2nd Row Text
            Text(
                text = "Create Account",
                fontSize = 32.sp,
                fontFamily = manropeFontFamily,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Just a few things to get started.",
                fontSize = 16.sp,
                fontFamily = manropeFontFamily,
                fontWeight = FontWeight.Light
            )

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Name
                Text(
                    text = "Full Name",
                    fontFamily = manropeFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(4.dp))

                AuthTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    hint = "John Doe",
                    leadingIcon = Icons.Default.Person
                )

                Spacer(Modifier.height(18.dp))

                // Email
                Text(
                    text = "Email Address",
                    fontFamily = manropeFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(4.dp))

                AuthTextField(
                    value = email,
                    onValueChange = { email = it },
                    hint = "name@company.com",
                    leadingIcon = Icons.Default.Email
                )

                Spacer(Modifier.height(18.dp))

                //Password
                Text(
                    text = "Password",
                    fontFamily = manropeFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(4.dp))

                AuthPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    hint = "Enter your password",
                    leadingIcon = Icons.Default.Lock
                )

                Spacer(Modifier.height(18.dp))

                if (authState is AuthState.Error) {
                    Text(
                        text = authState.message,
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = manropeFontFamily,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.height(8.dp))
                }

                AppButton(
                    text = "Create Account",
                    height = 55.dp,
                    enabled = !isLoading,
                    onClick = { onSignUp(email, password, fullName) }
                )

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color.LightGray
                    )

                    Text(
                        text = "OR CONTINUE WITH",
                        fontSize = 12.sp,
                        fontFamily = manropeFontFamily,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 6.dp),
                        color = Color.Gray
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color.LightGray
                    )
                }

                Spacer(Modifier.height(18.dp))

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    onClick = onSignUpWithGoogle,
                    enabled = !isLoading,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = colors.fill,
                        contentColor = colors.font
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, colors.stroke),
                    contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = "Google Icon",
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(Modifier.width(4.dp))

                        Text(
                            text = "Sign up with Google",
                            color = colors.font,
                            fontFamily = robotoFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Already have an account?",
                        fontFamily = manropeFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )

                    Text(
                        text = "Sign In",
                        modifier = Modifier.clickable(onClick = onNavigateToSignIn),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun Preview_SignUpScreen() {
    ContextaTheme(dynamicColor = false) {
        SignUpScreen(
            authState = AuthState.Unauthenticated,
            onSignUp = { _, _, _ -> },
            onSignUpWithGoogle = {},
            onNavigateToSignIn = {}
        )
    }
}