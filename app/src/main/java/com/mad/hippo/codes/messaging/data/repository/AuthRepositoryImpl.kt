package com.mad.hippo.codes.messaging.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mad.hippo.codes.messaging.domain.model.Response
import com.mad.hippo.codes.messaging.domain.repository.AuthRepository
import com.mad.hippo.codes.messaging.utils.KeyStoreUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
) : AuthRepository {

    override fun isUserAuthenticatedInFirebase() = auth.currentUser != null

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun firebaseSignInAnonymously() = flow {
        try {
            emit(Response.Loading)
            val task =  auth.signInAnonymously().await()
            Log.d("TAG", "firebaseSignInAnonymously: ${task.user?.uid}")
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "ERROR_MESSAGE"))
        }
    }

    override suspend fun firebaseSignWithGoogle(credential: AuthCredential) =  flow {
        try {
            emit(Response.Loading)
            auth.signInWithCredential(credential).await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "ERROR_MESSAGE"))
        }
    }

    override suspend fun signOut(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.apply {
                delete().await()
                emit(Response.Success(true))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "ERROR_MESSAGE"))
        }
    }

    override fun getFirebaseAuthState() = callbackFlow  {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun getUID(): String {
        return auth.currentUser?.uid ?: ""
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}