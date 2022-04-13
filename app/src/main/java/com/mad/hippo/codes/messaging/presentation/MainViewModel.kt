package com.mad.hippo.codes.messaging.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mad.hippo.codes.messaging.domain.use_case.UseCases
import kotlinx.coroutines.Dispatchers

class MainViewModel (
    private val useCases: UseCases
): ViewModel() {
    val isUserAuthenticated get() = useCases.isUserAuthenticated()

}