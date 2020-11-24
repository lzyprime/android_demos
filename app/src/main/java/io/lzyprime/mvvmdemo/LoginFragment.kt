package io.lzyprime.mvvmdemo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.lzyprime.mvvmdemo.databinding.FragmentLoginBinding
import io.lzyprime.mvvmdemo.utils.Net
import io.lzyprime.mvvmdemo.viewmodels.ListPhotoViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: ListPhotoViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.loginBtn.setOnClickListener {
            val text = binding.accessKeyEidtText.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(context, "不能为空!", Toast.LENGTH_SHORT).show()
            } else {
                Net.ACCESS_KEY = text
                viewModel.refreshListPhotos()
            }
        }
    }
}