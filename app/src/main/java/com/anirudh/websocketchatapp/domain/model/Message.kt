package com.anirudh.websocketchatapp.domain.model

import java.time.LocalDateTime

data class Message(
    val text: String,
    val formattedTime: String,
    val username: String
)