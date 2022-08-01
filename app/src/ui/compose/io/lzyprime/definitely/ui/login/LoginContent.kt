package io.lzyprime.definitely.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.lzyprime.definitely.R
import io.lzyprime.definitely.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    loginViewModel: LoginViewModel = viewModel()
) {
    val uiState by loginViewModel.loginUiState.collectAsState()
    var showPassword by remember { mutableStateOf(false) }

    Scaffold(Modifier.fillMaxSize()) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                label = { Text(text = stringResource(id = R.string.username)) },
                value = uiState.username,
                enabled = !uiState.isLoading,
                singleLine = true,
                onValueChange = { loginViewModel.editUsername(it) },
            )

            OutlinedTextField(
                label = { Text(text = stringResource(id = R.string.password)) },
                value = uiState.password,
                enabled = !uiState.isLoading,
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            painter = painterResource(id = if (showPassword) R.drawable.ic_visibility_off else R.drawable.ic_visibility),
                            null
                        )
                    }
                },
                onValueChange = { loginViewModel.editPassword(it) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                enabled = uiState.enableLoginBtn,
                onClick = { loginViewModel.login() }
            ) {
                Text(text = stringResource(id = R.string.login))
            }
        }
    }
}