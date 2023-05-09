package com.anirudh.websocketchatapp.di

import com.anirudh.websocketchatapp.data.remote.service.ChatSocketService
import com.anirudh.websocketchatapp.data.remote.service.ChatSocketServiceImpl
import com.anirudh.websocketchatapp.data.remote.service.MessageService
import com.anirudh.websocketchatapp.data.remote.service.MessageServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient():HttpClient{
        return HttpClient(CIO){
            engine {
                https {
                    trustManager = object: X509TrustManager {
                        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) { }

                        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) { }

                        override fun getAcceptedIssuers(): Array<X509Certificate>? = null
                    }
                }
            }
            install(Logging)
            install(WebSockets)
            install(JsonFeature){
                serializer = KotlinxSerializer()
            }
        }
    }

    @Provides
    @Singleton
    fun provideMessageService(client:HttpClient) : MessageService{
        return MessageServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideChatService(client:HttpClient):ChatSocketService{
        return ChatSocketServiceImpl(client)
    }
}