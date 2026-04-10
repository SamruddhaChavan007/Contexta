package com.example.contexta

import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.contexta.auth.state.SessionState
import com.example.contexta.auth.viewmodel.AuthViewModel
import com.example.contexta.data.theme.ThemePreference
import com.example.contexta.moreoptions.viewmodel.MoreOptionsViewModel
import com.example.contexta.navigation.NavGraph
import com.example.contexta.ui.theme.ContextaTheme
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var supabaseClient: SupabaseClient
    private val authViewModel: AuthViewModel by viewModels()
    private val themeViewModel: MoreOptionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            supabaseClient.handleDeeplinks(intent)
        }

        splashScreen.setKeepOnScreenCondition {
            authViewModel.sessionState.value is SessionState.Unknown
        }

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val iconView = splashScreenView.iconView

            val scaleX = ObjectAnimator.ofFloat(iconView, android.view.View.SCALE_X, 1f, 15f)
            val scaleY = ObjectAnimator.ofFloat(iconView, android.view.View.SCALE_Y, 1f, 15f)

            val fadeOut =
                ObjectAnimator.ofFloat(splashScreenView.view, android.view.View.ALPHA, 1f, 0f)

            AnimatorSet().apply {
                interpolator = AccelerateInterpolator()
                duration = 600L
                playTogether(scaleX, scaleY, fadeOut)

                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: android.animation.Animator) {
                        splashScreenView.remove()
                    }
                })
                start()
            }
        }

        enableEdgeToEdge()
        setContent {
            val themePreference by themeViewModel.themePreference.collectAsStateWithLifecycle()
            val isDark = when (themePreference) {
                ThemePreference.LIGHT  -> false
                ThemePreference.DARK   -> true
                ThemePreference.SYSTEM -> isSystemInDarkTheme()
            }
            ContextaTheme(darkTheme = isDark, dynamicColor = false) {
                NavGraph()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        lifecycleScope.launch {
            supabaseClient.handleDeeplinks(intent)
        }
    }
}