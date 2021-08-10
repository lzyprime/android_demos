package io.lzyprime.definitelyCompose.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.lzyprime.core.R
import io.lzyprime.core.model.bean.IMLoginState
import io.lzyprime.core.utils.GenerateTestUserSig
import io.lzyprime.core.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginContent(
    userId: String,
    model: UserViewModel,
) {
    val scope = rememberCoroutineScope()
    val loginState by model.loginState.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val generateSigFailed = stringResource(id = R.string.generate_sig_failed)

    ProvideLoginContent(
        scaffoldState = scaffoldState,
        initUserId = userId,
        loginState = loginState,
    ) {
        val sig = GenerateTestUserSig.genTestUserSig(userId)
        if (sig.isNotEmpty()) {
            model.login(userId, sig)
        } else {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(generateSigFailed)
            }
        }
    }
}

@Composable
private fun ProvideLoginContent(
    scaffoldState: ScaffoldState,
    initUserId: String,
    loginState: IMLoginState,
    onClick: () -> Unit,
) {
    var userId by remember { mutableStateOf(initUserId) }
    val loginBtnEnable by derivedStateOf { userId.isNotEmpty() && loginState != IMLoginState.LoggingIn }

    Scaffold(scaffoldState = scaffoldState) {
        Box(
            Modifier
                .height(240.dp)
                .fillMaxWidth(),
            Alignment.Center,
        ) {
            Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.h3)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            OutlinedTextField(
                label = {
                    Text(text = stringResource(R.string.userId))
                },
                singleLine = true,
                value = userId,
                onValueChange = {
                    userId = it
                },
            )
            Button(
                modifier = Modifier.padding(vertical = 12.dp),
                enabled = loginBtnEnable,
                onClick = onClick,
            ) {
                Text(text = stringResource(R.string.login))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ProvideLoginContent(
        scaffoldState = rememberScaffoldState(),
        initUserId = "init userId",
        loginState = IMLoginState.NoLogin,
    ) {}
}