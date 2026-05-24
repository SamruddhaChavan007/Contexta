package com.example.contexta.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class StrategyScreenViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(StrategyUiState())
    val uiState: StateFlow<StrategyUiState> = _uiState.asStateFlow()

    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    init {
        startClock()
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {

                val current = LocalDateTime.now()

                val greeting = when (current.hour) {
                    in 0..11 -> "GOOD MORNING"
                    in 12..16 -> "GOOD AFTERNOON"
                    else -> "GOOD EVENING"
                }

                val dayName = current.dayOfWeek.getDisplayName(
                    java.time.format.TextStyle.FULL,
                    java.util.Locale.getDefault()
                )

                _uiState.value = StrategyUiState(
                    dayName = dayName.uppercase(),
                    currentTime = current.format(formatter),
                    greeting = greeting
                )

                delay(1000)
            }
        }
    }

}