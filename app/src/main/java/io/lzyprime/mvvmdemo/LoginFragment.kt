package io.lzyprime.mvvmdemo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.lzyprime.mvvmdemo.utils.Net
import io.lzyprime.mvvmdemo.viewmodels.ListPhotoViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: ListPhotoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginBtn.setOnClickListener {
            val text = access_key_eidt_text.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(context, "不能为空!", Toast.LENGTH_SHORT).show()
            } else {
                Net.ACCESS_KEY = text
                viewModel.refreshListPhotos()
            }
        }
    }
}