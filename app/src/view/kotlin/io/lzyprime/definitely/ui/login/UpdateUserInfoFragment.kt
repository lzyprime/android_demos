package io.lzyprime.definitely.ui.login

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import dagger.hilt.android.AndroidEntryPoint

import io.lzyprime.definitely.R
import io.lzyprime.definitely.databinding.UpdateUserinfoFragmentBinding
import io.lzyprime.definitely.ui.dialogs.SelectListDialog
import io.lzyprime.definitely.ui.utils.*
import io.lzyprime.definitely.utils.PermissionState
import io.lzyprime.definitely.utils.launchWithRepeat
import io.lzyprime.definitely.utils.registerCheckPermissionLauncher
import io.lzyprime.definitely.utils.toByteArray
import io.lzyprime.definitely.viewmodel.UpdateUserInfoViewModel
import io.lzyprime.svr.model.Gender
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class UpdateUserInfoFragment : Fragment(R.layout.update_userinfo_fragment) {
    private val binding by viewBinding<UpdateUserinfoFragmentBinding>()
    private val model by viewModels<UpdateUserInfoViewModel>()

    private val checkPermissionLauncher = registerCheckPermissionLauncher()
    private val takePictureLauncher = registerTakePicturePreviewLauncher()
    private val gentContentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if(it != null) {
            context?.contentResolver?.openInputStream(it)?.use { stream ->
                stream.readBytes()
            }?.let { b ->
                model.updateAvatar(b)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            model.initData()
        }.invokeOnCompletion {
            val state = model.userInfoUiState.value
            binding.nicknameInput.editText?.setText(state.nickname)
            when (state.gender) {
                Gender.Male -> binding.male
                Gender.Female -> binding.female
                Gender.Secret -> binding.secret
                Gender.Unknown -> null
            }?.isChecked = true
        }
        avatarImageView()
        nicknameInput()
        selectGender()
        submitButton()
    }

    private fun avatarImageView() {
        viewLifecycleOwner.launchWithRepeat {
            model.userInfoUiState.map { it.avatar }
                .collectLatest { avatar ->
                    binding.avatarImageView.scaleType =
                        if (avatar.isBlank()) ImageView.ScaleType.CENTER else ImageView.ScaleType.CENTER_CROP
                    binding.avatarImageView.load(avatar.ifBlank { R.drawable.ic_logo })
                }
        }
        binding.avatarImageView.setOnClickListener {
            if (!model.userInfoUiState.value.updatingAvatar) {
                SelectListDialog(arrayOf(R.string.from_camera, R.string.from_file)) {
                    when (it) {
                        0 -> updateAvatarFromCamera()
                        1 -> updateAvatarFromFile()
                    }
                }.show(childFragmentManager, null)
            }
        }
    }

    private fun nicknameInput() {
        binding.nicknameInput.editText?.doOnTextChanged { text, _, _, _ ->
            model.updateNickname(text?.toString().orEmpty())
        }
    }

    private fun selectGender() {
        binding.selectGender.setOnCheckedChangeListener { _, i ->
            model.updateGender(
                when (i) {
                    R.id.male -> Gender.Male
                    R.id.female -> Gender.Female
                    R.id.secret -> Gender.Secret
                    else -> Gender.Unknown
                }
            )
        }
    }

    private fun submitButton() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            model.userInfoUiState.map { it.submitButtonEnable && !it.loading }
                .collect {
                    binding.submitButton.isEnabled = it
                }
        }
        binding.submitButton.setOnClickListener {
            model.updateUserInfo()
        }
    }

    private fun updateAvatarFromCamera() {
        checkPermissionLauncher(Manifest.permission.CAMERA) { state ->
            when (state) {
                PermissionState.GRANTED -> {
                    takePictureLauncher {
                        if (it != null) {
                            model.updateAvatar(it.toByteArray())
                        }
                    }
                }
                PermissionState.SHOW_RATIONALE -> {}
                PermissionState.DENIED -> {}
            }
        }
    }
    private fun updateAvatarFromFile() {
        gentContentLauncher.launch("image/*")
    }
}