package io.lzyprime.core.model.modules

import android.app.Application
import com.tencent.imsdk.v2.V2TIMManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.lzyprime.core.model.api.IMAuthService
import io.lzyprime.core.model.api.IMConversationService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IMServiceModules {

    @Singleton
    @Provides
    fun provideAuthService(application: Application): IMAuthService =
        IMAuthService(application, V2TIMManager.getInstance())

    @Singleton
    @Provides
    fun provideConversationService(
        @ApplicationScope scope: CoroutineScope,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
    ): IMConversationService =
        IMConversationService(
            scope,
            ioDispatcher,
            V2TIMManager.getConversationManager(),
        )

}