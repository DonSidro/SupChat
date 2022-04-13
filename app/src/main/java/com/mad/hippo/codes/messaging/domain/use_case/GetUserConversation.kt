package com.mad.hippo.codes.messaging.domain.use_case

import com.mad.hippo.codes.messaging.domain.repository.AuthRepository
import com.mad.hippo.codes.messaging.domain.repository.FirestoreRepository

class GetUserConversation(
    private val repository: FirestoreRepository
) {
    operator fun invoke() = repository.getConversationForUser()
}