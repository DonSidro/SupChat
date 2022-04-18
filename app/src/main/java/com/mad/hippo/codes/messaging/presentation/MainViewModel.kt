package com.mad.hippo.codes.messaging.presentation

import androidx.lifecycle.ViewModel
import com.mad.hippo.codes.messaging.domain.use_case.UseCases

class MainViewModel (
    private val useCases: UseCases
): ViewModel() {
    val isUserAuthenticated get() = useCases.isUserAuthenticated()

}