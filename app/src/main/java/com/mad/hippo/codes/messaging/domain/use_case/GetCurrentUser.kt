package com.mad.hippo.codes.messaging.domain.use_case

import com.mad.hippo.codes.messaging.domain.repository.AuthRepository

class GetCurrentUser(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.getCurrentUser()
}