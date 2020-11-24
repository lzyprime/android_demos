package io.lzyprime.mvvmdemo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.mvvmdemo.R
import io.lzyprime.mvvmdemo.extension.toast
import io.lzyprime.mvvmdemo.utils.*
import io.lzyprime.mvvmdemo.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.time.format.TextStyle

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val model by activityViewModels<UserViewModel>()
    private val userIdState = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStore = requireContext().dataStore
        lifecycleScope.launch {
            userIdState.value = dataStore.get(USER_ID, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                LoginContent()
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun LoginContent() {
        var userId by remember { userIdState }
        val scaffoldState = rememberScaffoldState()
        Scaffold(scaffoldState = scaffoldState) {
            Box(Modifier.height(240.dp).fillMaxWidth(), Alignment.Center) {
                Text(text = stringResource(id = R.string.app_name), fontSize = 30.sp)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
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
                    enabled = userId.isNotEmpty(),
                    onClick = {
                        val sig = GenerateTestUserSig.genTestUserSig(userId)
                        if (sig.isNotEmpty()) {
                            val dataStore = requireContext().dataStore
                            lifecycleScope.launch {
                                dataStore.set(USER_ID, userId)
                                if (model.login(userId, sig)) {
                                    dataStore.set(SIG, sig)
                                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPhotoListFragment())
                                }
                            }
                        } else {
                            context toast getString(R.string.generate_sig_failed)
                        }
                    },
                ) {
                    Text(text = stringResource(R.string.login))
                }
            }
        }
    }
}