package com.mad.hippo.codes.messaging.domain.use_case

import com.mad.hippo.codes.messaging.domain.repository.FirestoreRepository

class GetOtherUser(
    private val repository: FirestoreRepository
) {
    operator fun invoke(otherUserId: String) = repository.getOtherUser(otherUserId)
}