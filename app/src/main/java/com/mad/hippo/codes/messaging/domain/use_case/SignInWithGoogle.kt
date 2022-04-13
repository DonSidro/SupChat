package com.mad.hippo.codes.messaging.domain.use_case

import com.google.firebase.auth.AuthCredential
import com.mad.hippo.codes.messaging.domain.repository.AuthRepository

class SignInWithGoogle(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(credential : AuthCredential) = repository.firebaseSignWithGoogle(credential = credential)
}