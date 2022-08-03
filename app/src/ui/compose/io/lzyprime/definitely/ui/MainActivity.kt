package io.lzyprime.definitely.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitely.R
import io.lzyprime.definitely.ui.login.LoginContent
import io.lzyprime.definitely.ui.login.UpdateUserInfoContent
import io.lzyprime.definitely.ui.theme.DefinitelyTheme
import io.lzyprime.definitely.utils.launchWithRepeat
import io.lzyprime.definitely.viewmodel.UserViewModel
import io.lzyprime.svr.model.LoginState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel by viewModels<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        lifecycleScope.launchWhenCreated {
            val state = userViewModel.loginState.filter { it != LoginState.Loading }.first()
            setContent {
                MainContent(state = state)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MainContent(state: LoginState) {
        var loginState by remember { mutableStateOf(state) }
        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        launchWithRepeat {
            userViewModel.loginState.filter { it != LoginState.Loading }.collectLatest {
                loginState = it
            }
        }

        val reEnterExit = stringResource(id = R.string.re_enter_press_again_to_exit)
        onBackPressedDispatcher.addCallback {
            if (snackBarHostState.currentSnackbarData != null)
                finish()
            else
                scope.launch { snackBarHostState.showSnackbar(reEnterExit) }
        }

        DefinitelyTheme {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
                },
            ) {
                when (val cur = loginState) {
                    is LoginState.LoginUserInfo -> if (cur.needComplete) {
                        UpdateUserInfoContent()
                    } else {
                        MainNavHost()
                    }
                    else -> LoginContent()
                }
            }
        }
    }
}

@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController()
) {
//    NavHost(
//        navController = navController,
//        startDestination = "/home",
//    ) {
//
//    }
}