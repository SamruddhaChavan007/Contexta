package com.example.contexta.auth.validation

import android.util.Patterns

object AuthValidator {
    fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Email is required"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Enter a valid email address"
        else -> null
    }

    fun validatePassword(password: String): String? = when {
        password.isBlank() -> "Password is required"
        password.length < 8 -> "Password must be at least 8 characters"
        else -> null
    }

    fun validateFullName(name: String): String? = when {
        name.isBlank() -> "Full name is required"
        else -> null
    }
}
