package io.lzyprime.definitely.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitely.ui.login.LoginContent
import io.lzyprime.definitely.ui.login.UpdateUserInfoContent
import io.lzyprime.definitely.ui.theme.DefinitelyTheme
import io.lzyprime.definitely.utils.launchWithRepeat
import io.lzyprime.definitely.viewmodel.UserViewModel
import io.lzyprime.svr.model.LoginState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel by viewModels<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this,)
        lifecycleScope.launchWhenCreated {
            val state = userViewModel.loginState.filter { it != LoginState.Loading }.first()
            setContent {
                DefinitelyTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainContent(state = state)
                    }
                }
            }
        }
    }

    @Composable
    private fun MainContent(state: LoginState) {
        var loginState by remember { mutableStateOf(state) }
        launchWithRepeat {
            userViewModel.loginState.filter { it != LoginState.Loading }.collectLatest {
                loginState = it
            }
        }
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