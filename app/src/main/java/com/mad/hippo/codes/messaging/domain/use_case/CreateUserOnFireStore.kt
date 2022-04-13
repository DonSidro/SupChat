package com.mad.hippo.codes.messaging.domain.use_case

import com.mad.hippo.codes.messaging.domain.repository.FirestoreRepository

class CreateUserOnFireStore(
    private val repository: FirestoreRepository
) {
     operator fun invoke(publicKey: String , onComplete : () -> Unit) =
        repository.initCurrentUserIfFirstTime(publicKey= publicKey, onComplete = onComplete)

}