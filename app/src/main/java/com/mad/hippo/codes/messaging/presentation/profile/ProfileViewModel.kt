package com.mad.hippo.codes.messaging.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.hippo.codes.messaging.utils.Response
import com.mad.hippo.codes.messaging.domain.use_case.UseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val useCases: UseCases
): ViewModel() {
    private val _signOutState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val signOutState: State<Response<Boolean>> = _signOutState

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            useCases.signOut().collect { response ->
                _signOutState.value = response
            }
        }
    }

    fun onOpenDialogClicked() {
        _showDialog.value = true
    }


    fun onDialogDismiss() {
        _showDialog.value = false
    }


    fun getUID()  = useCases.getUID()
}