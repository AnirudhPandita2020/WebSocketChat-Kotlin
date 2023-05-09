package com.anirudh.websocketchatapp.data.remote.service

import com.anirudh.websocketchatapp.domain.model.Message

interface MessageService {
    suspend fun getAllMessages(): List<Message>

    companion object {
        const val BASE_URL = "https://14b8-14-99-188-242.ngrok-free.app"
    }

    sealed class Endpoints(val url: String) {
        object GetAllMessages : Endpoints("$BASE_URL/api/chat")
    }
}