package com.mad.hippo.codes.messaging.domain.use_case

import com.mad.hippo.codes.messaging.domain.repository.AuthRepository

class GetUID(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.getUID()
}