package io.lzyprime.definitely.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitely.R
import io.lzyprime.definitely.ui.login.LoginContent
import io.lzyprime.definitely.ui.login.UpdateUserInfoContent
import io.lzyprime.definitely.ui.theme.DefinitelyTheme
import io.lzyprime.definitely.viewmodel.UserViewModel
import io.lzyprime.svr.model.LoginState
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainContent()
        }
    }

    @Composable
    private fun SplashScreen() {
        BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            val size = minOf(maxHeight, maxWidth) / 2
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.size(size),
            )
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MainContent() {
        val loginState by userViewModel.loginState.collectAsState(initial = LoginState.Loading)
        if(loginState == LoginState.Loading) {
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
                        if(loginState == LoginState.Logout) {
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
