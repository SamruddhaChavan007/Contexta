package com.example.contexta.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = hint,
                color = Color(0xFF8E9193) // Hardcoded as requested
            )
        },
        modifier = modifier.fillMaxWidth(),
        // Switches between dots (••••) and plain text
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        // Ensures the keyboard optimized for passwords (e.g., no autocorrect)
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFEFF1F2),
            focusedContainerColor = Color(0xFFEFF1F2),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color(0xFF8E9193)
            )
        },
        trailingIcon = {
            val icon =
                if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (isPasswordVisible) "Hide password" else "Show password"

            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = icon,
                    contentDescription = description,
                    tint = Color(0xFF8E9193)
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Preview(name = "Hidden State", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AuthPasswordFieldPreview() {
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        AuthPasswordField(
            value = password,
            onValueChange = { password = it },
            hint = "Enter Password",
            leadingIcon = Icons.Default.Lock
        )
    }
}

@Preview(name = "Visible State", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AuthPasswordFieldVisiblePreview() {
    var text by remember { mutableStateOf("MyPassword123") }

    AuthPasswordField(
        value = text,
        onValueChange = { text = it },
        hint = "Password",
        leadingIcon = Icons.Default.Lock
    )
}