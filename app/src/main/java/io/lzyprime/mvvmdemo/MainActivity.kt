package io.lzyprime.mvvmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.mvvmdemo.databinding.ActivityMainBinding
import io.lzyprime.mvvmdemo.extension.viewBinding
import io.lzyprime.mvvmdemo.utils.*
import io.lzyprime.mvvmdemo.viewmodel.ListPhotoViewModel
import io.lzyprime.mvvmdemo.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: ListPhotoViewModel by viewModels()
    private val userModel by viewModels<UserViewModel>()
    private val binding by viewBinding<ActivityMainBinding>()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.mainNavHost) as NavHostFragment
        navController = navHost.findNavController()


        onBackPressedDispatcher.addCallback(this) {
            if (!navController.navigateUp()) {
                finish()
            }
        }

        initGraph()
    }

    private fun initGraph() {
        val graph = navController.navInflater.inflate(R.navigation.main_graph)
        val dataStore = dataStore
        lifecycleScope.launch {
            val userId = dataStore.get(USER_ID)
            val sig = dataStore.get(SIG)
            graph.startDestination =
                if (userId.isNullOrEmpty() ||
                    sig.isNullOrEmpty() ||
                    !userModel.login(userId, sig)
                ) {
                    R.id.loginFragment
                } else {
                    R.id.photoList
                }
        }.invokeOnCompletion {
            navController.graph = graph
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // 是否显示返回按钮
        return navController.previousBackStackEntry != null || super.onSupportNavigateUp()
    }
}