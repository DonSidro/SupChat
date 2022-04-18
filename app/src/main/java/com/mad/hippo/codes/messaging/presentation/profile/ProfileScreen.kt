package com.mad.hippo.codes.messaging.presentation.profile

import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.mad.hippo.codes.messaging.utils.Response
import com.mad.hippo.codes.messaging.presentation.components.ProgressBar
import com.mad.hippo.codes.messaging.presentation.navigation.Screen
import com.mad.hippo.codes.messaging.presentation.profile.components.ProfileContent
import com.mad.hippo.codes.messaging.presentation.profile.components.ProfileTopBar
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.compose.getViewModel

@Composable
@InternalCoroutinesApi
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = getViewModel()
) {
    var uid by remember { mutableStateOf(viewModel.getUID()) }

    Scaffold(
        topBar = {
            ProfileTopBar()
        }
    ) {
        ProfileContent(uid = uid)
    }

    when(val response = viewModel.signOutState.value) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> if (response.data) {
            LaunchedEffect(response.data) {
                navController.popBackStack()
                navController.navigate(Screen.AuthScreen.route)
            }
        }
        is Error -> LaunchedEffect(Unit) {

        }
        else -> {}
    }

}

