package com.anirudh.websocketchatapp.data.remote.service

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.anirudh.websocketchatapp.data.remote.dto.MessageDto
import com.anirudh.websocketchatapp.domain.model.Message
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class MessageServiceImpl @Inject constructor(
    private val client: HttpClient
) : MessageService {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAllMessages(): List<Message> {
        return try {
            client.get<List<MessageDto>>(MessageService.Endpoints.GetAllMessages.url)
                .map { it.toMessage() }
        } catch (e: Exception) {
            Log.e("ERROR IN CALL",e.localizedMessage)
            emptyList()
        }
    }
}