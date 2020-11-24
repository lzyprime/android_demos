package io.lzyprime.mvvmdemo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.lzyprime.mvvmdemo.databinding.FragmentLoginBinding
import io.lzyprime.mvvmdemo.utils.Net
import io.lzyprime.mvvmdemo.utils.toast
import io.lzyprime.mvvmdemo.utils.viewBinding
import io.lzyprime.mvvmdemo.viewmodels.ListPhotoViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: ListPhotoViewModel by activityViewModels()
    private val binding by viewBinding<FragmentLoginBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener {
            val text = binding.accessKeyEidtText.text.toString()
            if (text.isEmpty()) {
                context toast "不能为空!"
            } else {
                Net.ACCESS_KEY = text
                viewModel.refreshListPhotos()
            }
        }
    }
}