package com.anirudh.websocketchatapp.presentation.chat

import com.anirudh.websocketchatapp.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false
)
