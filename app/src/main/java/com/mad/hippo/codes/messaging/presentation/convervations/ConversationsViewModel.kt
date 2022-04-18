package com.mad.hippo.codes.messaging.presentation.convervations

import android.content.Context
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.hippo.codes.messaging.domain.model.Conversation
import com.mad.hippo.codes.messaging.domain.model.Message
import com.mad.hippo.codes.messaging.utils.Response
import com.mad.hippo.codes.messaging.domain.model.User
import com.mad.hippo.codes.messaging.domain.use_case.UseCases
import com.mad.hippo.codes.messaging.utils.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.security.Key
import java.security.KeyPair

class ConversationsViewModel (private val useCases: UseCases
): ViewModel() {

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    val list = MutableStateFlow<Response<List<Conversation>>>(Response.IDLE)
    val listMessage = MutableStateFlow<Response<List<Message>>>(Response.IDLE)

    val latestConversationID = MutableStateFlow<Response<Any>>(Response.IDLE)

    val sendStatus = MutableStateFlow<Response<Boolean>>(Response.IDLE)

    val currentUser = useCases.getCurrentUser.invoke()

    private val _otherUser = MutableStateFlow(User())
    val otherUser: StateFlow<User> = _otherUser.asStateFlow()


    fun onOpenDialogClicked() {
        _showDialog.value = true
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }

    fun getConversationList() = viewModelScope.launch {
        useCases.getUserConversation().collect{
            list.emit(it)
        }
    }

    fun getConversationMessages(conversationId :String) = viewModelScope.launch {
        useCases.getConversationMessages(conversationId = conversationId).collect{
            listMessage.emit(it)
        }
    }

    fun getOrCreateConversation(otherUserID : String) = viewModelScope.launch {
        useCases.createConversation(otherUserID).collect {
            latestConversationID.emit(it)
        }
    }

    fun sendMessage(message: Message, conversationId: String) {
        viewModelScope.launch {
            useCases.sendMessage(message = message, conversationId = conversationId).collect { response ->
                sendStatus.emit(response)
            }
        }
    }

    fun getOtherUser(otherUserID: String) = viewModelScope.launch {
        useCases.getOtherUser(otherUserID).collectLatest {
            _otherUser.emit(it)
        }
    }

}