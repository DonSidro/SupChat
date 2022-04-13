package com.mad.hippo.codes.messaging.domain.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.mad.hippo.codes.messaging.domain.model.Response
import com.mad.hippo.codes.messaging.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun isUserAuthenticatedInFirebase(): Boolean

    suspend fun firebaseSignInAnonymously(): Flow<Response<Boolean>>

    suspend fun firebaseSignWithGoogle(credential: AuthCredential): Flow<Response<Boolean>>

    suspend fun signOut(): Flow<Response<Boolean>>

    fun getFirebaseAuthState(): Flow<Boolean>

    fun getUID(): String

    fun getCurrentUser() : FirebaseUser?

}