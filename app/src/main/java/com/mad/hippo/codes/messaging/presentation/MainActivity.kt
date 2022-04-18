package com.mad.hippo.codes.messaging.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mad.hippo.codes.messaging.presentation.navigation.NavGraph
import com.mad.hippo.codes.messaging.presentation.navigation.Screen
import com.mad.hippo.codes.messaging.utils.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModel()
    lateinit var navController : NavHostController

    @RequiresApi(Build.VERSION_CODES.M)
    @OptIn(InternalCoroutinesApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val e = KeyStoreUtil.encryptString("Hej med dig", KeyStoreUtil.getPublicKeyAsString())
        val d = e?.let { KeyStoreUtil.decryptString(it) }
        Log.d(TAG, "onCreate: $d")


        setContent {
            navController = rememberAnimatedNavController()
            NavGraph(
                navController = navController
            )
            if (mainViewModel.isUserAuthenticated) {
                navController.navigate(Screen.ConversationsScreen.route)
            }
        }

    }
}