package io.lzyprime.core.data

import io.lzyprime.core.data.models.LoginState
import io.lzyprime.core.data.models.User
import io.lzyprime.core.data.modules.ApplicationScope
import io.lzyprime.core.data.modules.IODispatcher
import io.lzyprime.core.data.sources.UnsplashLocalDataSource
import io.lzyprime.core.data.sources.UnsplashRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val unsplashLocalDataSource: UnsplashLocalDataSource,
    private val unsplashRemoteDataSource: UnsplashRemoteDataSource,
    @ApplicationScope applicationScope: CoroutineScope,
    @IODispatcher ioDispatcher: CoroutineDispatcher,
) {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.NotLogin)
    val loginState = _loginState.asStateFlow()

    private var _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    init {
        applicationScope.launch(ioDispatcher) {
            loginState.filter { it == LoginState.Logged }.collect {
                refreshUser()
            }
        }
    }


    suspend fun login(userCode: String) {
        if (_loginState.value is LoginState.NotLogin) {
            _loginState.value = LoginState.Loading
            unsplashRemoteDataSource.login(userCode).onSuccess {
                unsplashRemoteDataSource.setToken(it.access_token)
                unsplashLocalDataSource.updateToken(it.access_token)
                _loginState.value = LoginState.Logged
            }.onFailure {
                _loginState.value = LoginState.NotLogin.OauthTokenFailed
            }
        }
    }

    suspend fun autoLogin() {
        if (_loginState.value is LoginState.NotLogin) {
            _loginState.value = LoginState.Loading
            val token = unsplashLocalDataSource.getToken()
            unsplashRemoteDataSource.setToken(token)
            _loginState.value = if (token.isEmpty()) LoginState.NotLogin else LoginState.Logged
        }
    }

    suspend fun refreshUser() {
         unsplashRemoteDataSource.getUser().map {
            User(
                id = it.id,
                username = it.username,
                avatar = it.profile_image.medium,
                email = it.email,
                updateAt = it.updated_at,
                collections = it.total_collections,
                likes = it.total_likes,
                photos = it.total_photos,
                followers = it.followers_count,
                following = it.following_count,
                isFollowed = it.followed_by_user,
                downloads = it.downloads,
                uploadsRemaining = it.uploads_remaining,
            )
        }.onSuccess {
             _user.value = it
         }
    }
}