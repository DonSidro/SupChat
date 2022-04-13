package com.mad.hippo.codes.messaging.domain.use_case

import com.mad.hippo.codes.messaging.domain.model.Message
import com.mad.hippo.codes.messaging.domain.repository.AuthRepository
import com.mad.hippo.codes.messaging.domain.repository.FirestoreRepository

class SendMessage(
    private val repository: FirestoreRepository
) {
    operator fun invoke(message : Message, conversationId : String) = repository.sendMessage(message, conversationId)
}