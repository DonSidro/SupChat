package com.mad.hippo.codes.messaging.presentation.auth

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.mad.hippo.codes.messaging.utils.Response
import com.mad.hippo.codes.messaging.presentation.auth.components.AuthContent
import com.mad.hippo.codes.messaging.presentation.auth.components.AuthTopBar
import com.mad.hippo.codes.messaging.presentation.navigation.Screen
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

private const val TAG = "AuthScreen"
@OptIn(InternalCoroutinesApi::class)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = getViewModel(),
    navController: NavController,
    googleSignInClient: GoogleSignInClient = get()
) {
    val context = LocalContext.current

    val state by viewModel.loginState.collectAsState()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            viewModel.signWithCredential(credential)
        } catch (e: ApiException) {
            Log.w("TAG", "Google sign in failed", e)
        }
    }

    Scaffold(
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                AuthTopBar()
                if (state == Response.Loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    ) {
        AuthContent(){
            launcher.launch(googleSignInClient.signInIntent)
        }
    }


    when(state) {
        is Response.Error -> {
            Toast.makeText(context, "Error Logging in!", Toast.LENGTH_SHORT).show()
        }
        Response.IDLE -> {}
        Response.Loading -> {}
        is Response.Success -> {
            if ((state as Response.Success<Boolean>).data) {
                LaunchedEffect((state as Response.Success<Boolean>).data) {
                    viewModel.initCurrentUserIfFirstTime(context = context){
                        navController.navigate(Screen.ConversationsScreen.route)
                    }
                }
            }
        }
    }
}