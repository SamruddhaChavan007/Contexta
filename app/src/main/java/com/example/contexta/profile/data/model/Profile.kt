package com.example.contexta.profile.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    @SerialName("full_name")
    val fullName: String?,
    val email: String,
    val created_at: String
)
