package io.lzyprime.mvvmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.mvvmdemo.databinding.ActivityMainBinding
import io.lzyprime.mvvmdemo.viewmodels.ListPhotoViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: ListPhotoViewModel by viewModels()
    private var loginSuccess = false
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.mainNavHost) as NavHostFragment
        val navController = navHost.findNavController()

        appBarConfiguration = AppBarConfiguration(setOf(R.id.loginFragment, R.id.photoListFragment))
        // 顶部appBar
        setupActionBarWithNavController(navController, appBarConfiguration)

        viewModel.listPhotos.observe(this) {
            // 列表不为空,登录成功
            if (!loginSuccess && it.isNotEmpty()) {
                loginSuccess = true
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_loginFragment_to_photoListFragment)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // 是否显示返回按钮
        return binding.mainNavHost.findNavController().navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        // 系统返回事件
        if (!binding.mainNavHost.findNavController().popBackStack()) finish()
    }
}