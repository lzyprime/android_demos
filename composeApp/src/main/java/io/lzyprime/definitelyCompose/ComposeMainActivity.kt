package io.lzyprime.definitelyCompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitelyCompose.main.AddressBookContent
import io.lzyprime.definitelyCompose.main.ConversationContent

@AndroidEntryPoint
class ComposeMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setContent {
            MdcTheme {
                MainNavContent()
            }
        }
    }

    @Composable
    fun MainNavContent() {
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
                BottomNavigation {
                    val currentBackStackEntry by navController.currentBackStackEntryAsState()
                    MainNavGraph.values().forEach {
                        BottomNavigationItem(
                            selected = currentBackStackEntry?.destination?.route == it.route,
                            onClick = {
                                navController.navigate(it.route) {
                                    popUpTo(MainNavGraph.startDestination) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(it.icon, it.route) }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(
                modifier = Modifier.padding(padding),
                navController = navController,
                startDestination = MainNavGraph.startDestination,
                builder = MainNavGraph.builder,
            )
        }
    }


    enum class MainNavGraph(
        val route: String,
        val icon: ImageVector,
        val content: @Composable (NavBackStackEntry) -> Unit,
    ) {
        Conversation("conversation", Icons.Default.List, { ConversationContent{} }),
        AddressBook("address_book", Icons.Default.AccountBox, { AddressBookContent() });

        companion object {
            val startDestination = Conversation.route
            val builder: NavGraphBuilder.() -> Unit = {
                values().forEach {
                    composable(it.route, content = it.content)
                }
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun Preview() {
        MainNavContent()
    }
}