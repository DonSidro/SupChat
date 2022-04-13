package com.mad.hippo.codes.messaging.domain.use_case

import com.mad.hippo.codes.messaging.domain.repository.AuthRepository

class SignInAnonymously(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.firebaseSignInAnonymously()
}