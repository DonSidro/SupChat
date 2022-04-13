package com.mad.hippo.codes.messaging.presentation.auth

import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.mad.hippo.codes.messaging.R
import com.mad.hippo.codes.messaging.domain.model.Response
import com.mad.hippo.codes.messaging.presentation.auth.components.AuthContent
import com.mad.hippo.codes.messaging.presentation.auth.components.AuthTopBar
import com.mad.hippo.codes.messaging.presentation.components.ProgressBar
import com.mad.hippo.codes.messaging.presentation.navigation.Screen
import com.mad.hippo.codes.messaging.utils.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

private const val TAG = "AuthScreen"
@OptIn(InternalCoroutinesApi::class)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = getViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val token = stringResource(R.string.default_web_client_id)
    val state by viewModel.loadingState.collectAsState()


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
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            launcher.launch(googleSignInClient.signInIntent)
        }
    }



    when(state) {
        is Response.Error -> {
            Toast.makeText(context, "Error Loging in!", Toast.LENGTH_SHORT).show()
        }
        Response.IDLE -> {}
        Response.Loading -> {}
        is Response.Success -> {
            if ((state as Response.Success<Boolean>).data) {
                LaunchedEffect((state as Response.Success<Boolean>).data) {
                    Log.d(TAG, "AuthScreen: asdadasd")
                    val dataStoreManager = DataStoreManager(context.baseDataStore)
                    coroutineScope.launch {
                        viewModel.initCurrentUserIfFirstTime(dataStoreManager.getPreference(
                                PreferenceRequest(PUBLIC_KEY, "")
                            )
                        ) {
                            navController.navigate(Screen.OverviewScreen.route)
                        }
                    }
                }
            }
        }
    }
}