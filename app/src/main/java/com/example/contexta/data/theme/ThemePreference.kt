package com.example.contexta.data.theme

enum class ThemePreference {
    LIGHT, DARK, SYSTEM;

    companion object {
        fun fromOrdinal(ordinal: Int) = entries.getOrElse(ordinal) { SYSTEM }
    }
}
