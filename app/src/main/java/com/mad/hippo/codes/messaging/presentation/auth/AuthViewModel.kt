package com.mad.hippo.codes.messaging.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.mad.hippo.codes.messaging.utils.Response
import com.mad.hippo.codes.messaging.domain.use_case.UseCases
import com.mad.hippo.codes.messaging.utils.DataStoreManager
import com.mad.hippo.codes.messaging.utils.PUBLIC_KEY
import com.mad.hippo.codes.messaging.utils.PreferenceRequest
import com.mad.hippo.codes.messaging.utils.baseDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val useCases: UseCases
): ViewModel() {

    val loginState = MutableStateFlow<Response<Boolean>>(Response.IDLE)

    //Anonymously Sign In
    fun signIn() {
        viewModelScope.launch {
            useCases.signInAnonymously().collect { response ->
                loginState.emit(response)
            }
        }
    }
    //Google Sign In
    fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
        useCases.signInWithGoogle(credential).collect{
            loginState.emit(it)
        }
    }

    //Creates User on Firestore DB if they are not registered
    fun initCurrentUserIfFirstTime(context : Context, onComplete : () -> Unit) = viewModelScope.launch{
        val dataStoreManager = DataStoreManager(context.baseDataStore)
        useCases.createUserOnFireStore(dataStoreManager.getPreference(
            PreferenceRequest(PUBLIC_KEY, "")), onComplete = onComplete)
    }
}