package io.lzyprime.definitely.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitely.R
import io.lzyprime.definitely.ui.login.LoginContent
import io.lzyprime.definitely.ui.login.UpdateUserInfoContent
import io.lzyprime.definitely.ui.theme.DefinitelyTheme
import io.lzyprime.definitely.viewmodel.NavUIState
import io.lzyprime.definitely.viewmodel.UserViewModel
import io.lzyprime.svr.model.LoginState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel by viewModels<UserViewModel>()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefinitelyTheme {
                val widthSizeClass = calculateWindowSizeClass(activity = this).widthSizeClass
                MainContent(widthSizeClass)
            }
        }
    }

    @Composable
    @Preview
    private fun SplashScreen() {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            contentScale = ContentScale.None,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary,
                        )
                    )
                ),
        )
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MainContent(widthSizeClass: WindowWidthSizeClass) {
        val isExpanded = widthSizeClass == WindowWidthSizeClass.Expanded
        val navUIState by userViewModel.navUIState.collectAsState()

        if (navUIState == NavUIState.SplashScreen) {
            SplashScreen()
        } else {
            val snackBarHostState = remember { SnackbarHostState() }
            val reEnterExit = stringResource(id = R.string.re_enter_press_again_to_exit)
            val scope = rememberCoroutineScope()
            BackHandler {
                if (snackBarHostState.currentSnackbarData != null) finish() else scope.launch {
                    snackBarHostState.showSnackbar(reEnterExit)
                }
            }

            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
                },
            ) { padding ->
                Box(Modifier.padding(padding)) {
                    when (navUIState) {
                        NavUIState.Login -> LoginContent()
                        NavUIState.UpdateUserInfo -> UpdateUserInfoContent()
                        else -> HomeNavHost(isExpanded)
                    }
                }
            }
        }
    }
}
