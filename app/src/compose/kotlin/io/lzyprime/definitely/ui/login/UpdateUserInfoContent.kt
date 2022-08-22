package io.lzyprime.definitely.ui.login

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.lzyprime.definitely.R
import io.lzyprime.definitely.ui.utils.*
import io.lzyprime.definitely.utils.toByteArray
import io.lzyprime.definitely.viewmodel.UpdateUserInfoViewModel
import io.lzyprime.definitely.viewmodel.UserInfoAction
import io.lzyprime.definitely.viewmodel.UserInfoUiState
import io.lzyprime.svr.model.Gender

@Composable
fun UpdateUserInfoContent(
    updateUserInfoViewModel: UpdateUserInfoViewModel = viewModel()
) {
    val uiState by updateUserInfoViewModel.uiState.collectAsState()
    Content(uiState = uiState, updateUserInfoViewModel::emitEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    uiState: UserInfoUiState,
    emitEvent: (UserInfoAction) -> Unit
) {
    val showUpdateAvatarDialog = remember { mutableStateOf(false) }
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable {
                        showUpdateAvatarDialog.value = true
                    }
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(value = uiState.nickname, onValueChange = {
                emitEvent(UserInfoAction.UpdateNickname(it))
            })
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = uiState.gender == Gender.Male,
                    enabled = uiState.loading,
                    onClick = {
                        emitEvent(UserInfoAction.UpdateGender(Gender.Male))
                    })
                Text(
                    text = stringResource(id = R.string.utf_male),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp
                )
                RadioButton(selected = uiState.gender == Gender.Female,
                    enabled = uiState.loading,
                    onClick = {
                        emitEvent(UserInfoAction.UpdateGender(Gender.Female))
                    })
                Text(
                    text = stringResource(id = R.string.utf_female),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 24.sp
                )
                RadioButton(selected = uiState.gender == Gender.Secret,
                    enabled = uiState.loading,
                    onClick = {
                        emitEvent(UserInfoAction.UpdateGender(Gender.Secret))
                    })
                Text(text = stringResource(id = R.string.secrecy))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                enabled = uiState.submitButtonEnable,
                onClick = {
                    emitEvent(UserInfoAction.UpdateUserInfo)
                }) {
                Text(stringResource(id = R.string.submit))
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    UpdateAvatarDialog(showDialog = showUpdateAvatarDialog, emitEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateAvatarDialog(
    showDialog: MutableState<Boolean>,
    emitEvent: (UserInfoAction) -> Unit,
) {
    val fromCamera = updateAvatarFromCamera(emitEvent)
    val fromFile = updateAvatarFromFile(emitEvent)

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ListItem(
                        modifier = Modifier.clickable {
                            fromCamera()
                            showDialog.value = false
                        },
                        headlineText = {
                            Text(
                                stringResource(id = R.string.from_camera),
                                Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth()
                            )
                        })

                    ListItem(
                        modifier = Modifier.clickable {
                            fromFile()
                            showDialog.value = false
                        },
                        headlineText = {
                            Text(
                                stringResource(id = R.string.from_file),
                                Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth()
                            )
                        })
                }
            }
        )
    }
}

@Composable
private fun updateAvatarFromCamera(
    emitEvent: (UserInfoAction) -> Unit,
): () -> Unit {
    val takePicturePreview = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = {
            it?.toByteArray()?.let { res ->
                emitEvent(UserInfoAction.UpdateAvatar(res))
            }
        },
    )
    val checkPermission = rememberCheckPermissionFunc()
    return {
        checkPermission(Manifest.permission.CAMERA) {
            if (it == PermissionStatus.GRANTED)
                takePicturePreview.launch(null)
        }
    }
}

@Composable
private fun updateAvatarFromFile(
    emitEvent: (UserInfoAction) -> Unit,
): () -> Unit {
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                context.contentResolver.openInputStream(it).use { stream ->
                    stream?.readBytes()?.let { bs ->
                        emitEvent(UserInfoAction.UpdateAvatar(bs))
                    }
                }
            }
        }
    )
    return {
        getContent.launch("image/*")
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content(
        UserInfoUiState(),
        {},
    )
}

@Preview
@Composable
fun PreviewDialog() {
    UpdateAvatarDialog(mutableStateOf(true), {})
}