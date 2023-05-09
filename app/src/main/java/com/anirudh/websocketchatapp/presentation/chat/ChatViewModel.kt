package com.anirudh.websocketchatapp.presentation.chat

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anirudh.websocketchatapp.data.remote.service.ChatSocketService
import com.anirudh.websocketchatapp.data.remote.service.MessageService
import com.anirudh.websocketchatapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private val _state = mutableStateOf(ChatState())
    val state: State<ChatState> = _state

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun connectToChat() {
        getAllMessages()
        savedStateHandle.get<String>("username")?.let { username ->
            viewModelScope.launch {
                when (val result = chatSocketService.initSession(username)) {
                    is Resource.Success -> {
                        chatSocketService.observeMessages().onEach { message ->
                            val newMessageList = state.value.messages.toMutableList().apply {
                                add(0,message)
                            }
                            _state.value = state.value.copy(
                                messages = newMessageList
                            )
                        }.launchIn(viewModelScope)
                    }
                    is Resource.Error -> {
                        _toastEvent.emit(result.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    fun onMessageChange(message: String) {
        _messageText.value = message
    }


    private fun getAllMessages() = viewModelScope.launch {
        _state.value = state.value.copy(isLoading = true)
        val result = messageService.getAllMessages()
        Log.e("result",result.toString())
        _state.value = state.value.copy(messages = result, isLoading = false)
    }

    fun disconnect() = viewModelScope.launch {
        chatSocketService.closeSession()
    }

    fun sendMessage() =
        viewModelScope.launch {
            if (messageText.value.isNotBlank())
                chatSocketService.sendMessage(messageText.value)
        }


    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}