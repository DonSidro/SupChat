package com.mad.hippo.codes.messaging.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.mad.hippo.codes.messaging.presentation.auth.AuthScreen
import com.mad.hippo.codes.messaging.presentation.chat.ChatScreen
import com.mad.hippo.codes.messaging.presentation.convervations.ConversationsScreen
import com.mad.hippo.codes.messaging.presentation.profile.ProfileScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@OptIn(ExperimentalAnimationApi::class)
fun NavGraph (
    navController: NavHostController
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.AuthScreen.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(
            route = Screen.AuthScreen.route
        ) {
            AuthScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.ProfileScreen.route
        ) {
            ProfileScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.ConversationsScreen.route
        ) {
            ConversationsScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.ChatScreen.route + "/{id}/{list}") { navBackStack ->
            val id = navBackStack.arguments?.getString("id")
            val list = navBackStack.arguments?.getString("list")

            ChatScreen(id,list,
                navController = navController
            )
        }
    }
}