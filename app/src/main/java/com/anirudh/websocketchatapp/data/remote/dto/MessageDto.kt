package com.anirudh.websocketchatapp.data.remote.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.anirudh.websocketchatapp.domain.model.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class MessageDto(
    val id: String,
    val text: String,
    val timeStamp: String,
    val username: String
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toMessage(): Message = Message(
        text = text,
        formattedTime = timeStamp.substring(0..9),
        username = username
    )
}