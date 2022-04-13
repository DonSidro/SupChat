package com.mad.hippo.codes.messaging.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.mad.hippo.codes.messaging.domain.model.Response
import com.mad.hippo.codes.messaging.domain.use_case.UseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val useCases: UseCases
): ViewModel() {

    val loadingState = MutableStateFlow<Response<Boolean>>(Response.IDLE)

    fun signIn() {
        viewModelScope.launch {
            useCases.signInAnonymously().collect { response ->
                loadingState.emit(response)
            }
        }
    }
    fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
        useCases.signInWithGoogle(credential).collect{
            loadingState.emit(it)
        }
    }

    fun initCurrentUserIfFirstTime(publicKey : String, onComplete : () -> Unit){
        useCases.createUserOnFireStore(publicKey, onComplete = onComplete)
    }
}