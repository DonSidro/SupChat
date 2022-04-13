package com.mad.hippo.codes.messaging.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mad.hippo.codes.messaging.presentation.navigation.NavGraph
import com.mad.hippo.codes.messaging.presentation.navigation.Screen
import com.mad.hippo.codes.messaging.utils.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext


class MainActivity : ComponentActivity(), CoroutineScope {
    private val mainViewModel: MainViewModel by viewModel()
    private var job: Job = Job()
    private var context: Context = this
    lateinit var navController : NavHostController

    @OptIn(InternalCoroutinesApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStoreManager = DataStoreManager(this.baseDataStore)
        launch {
            if (dataStoreManager.getPreference(PreferenceRequest(PRIVATE_KEY, "")).isEmpty()) {
                Log.d("TAG", "onCreate: asdasd")
                KeyStoreUtil.generateKeys(context)
            }
        }
        setContent {
            navController = rememberAnimatedNavController()
            NavGraph(
                navController = navController
            )
            if(mainViewModel.isUserAuthenticated) {
                navController.navigate(Screen.OverviewScreen.route)
            }
        }

    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}