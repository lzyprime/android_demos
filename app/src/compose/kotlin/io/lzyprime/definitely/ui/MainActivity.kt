package io.lzyprime.definitely.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitely.R
import io.lzyprime.definitely.ui.login.LoginContent
import io.lzyprime.definitely.ui.theme.DefinitelyTheme
import io.lzyprime.definitely.viewmodel.UserViewModel
import io.lzyprime.svr.model.LoginState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen()
        }
    }

    @Composable
    private fun SplashScreen() {
        SideEffect {
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
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
    private fun MainContent() {
        val loginState by userViewModel.loginState.collectAsState(initial = LoginState.Loading)
        if (loginState == LoginState.Loading) {
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
            DefinitelyTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    },
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        if (loginState == LoginState.Logout) {
                            LoginContent()
                        } else {
                            HomeNavHost()
                        }
                    }
                }
            }
        }
    }
}
