package com.mad.hippo.codes.messaging.domain.repository

import com.mad.hippo.codes.messaging.domain.model.Conversation
import com.mad.hippo.codes.messaging.domain.model.Message
import com.mad.hippo.codes.messaging.utils.Response
import com.mad.hippo.codes.messaging.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    fun initCurrentUserIfFirstTime(publicKey: String, onComplete : () -> Unit)

    fun getOrCreateConversation(otherUserId: String) : Flow<Response<Any>>

    fun getMessagesFromConversation(conversationID: String) : Flow<Response<List<Message>>>

    fun getConversationForUser() : Flow<Response<List<Conversation>>>

    fun sendMessage(message: Message, conversationID: String) : Flow<Response<Boolean>>

    fun getOtherUser(otherUserId: String) : Flow<User>

}