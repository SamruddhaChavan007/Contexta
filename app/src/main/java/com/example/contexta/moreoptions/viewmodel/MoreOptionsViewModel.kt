package com.example.contexta.moreoptions.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contexta.data.profile.ProfileLocalRepository
import com.example.contexta.data.theme.ThemePreference
import com.example.contexta.data.theme.ThemeRepository
import com.example.contexta.profile.data.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreOptionsViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val profileLocalRepository: ProfileLocalRepository
) : ViewModel() {

    val themePreference: StateFlow<ThemePreference> = themeRepository.themePreference
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemePreference.SYSTEM
        )

    val profile: StateFlow<Profile?> = profileLocalRepository.profileFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun setTheme(pref: ThemePreference) {
        viewModelScope.launch { themeRepository.setThemePreference(pref) }
    }
}
