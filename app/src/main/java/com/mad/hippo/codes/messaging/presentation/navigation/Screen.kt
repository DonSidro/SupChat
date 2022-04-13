package com.mad.hippo.codes.messaging.presentation.navigation

sealed class Screen(val route: String) {
    object AuthScreen: Screen("AUTH_SCREEN")
    object ProfileScreen: Screen("PROFILE_SCREEN")
    object ChatScreen: Screen("CHAT_SCREEN")
    object OverviewScreen: Screen("OVERVIEW_SCREEN")
    object ConversationScreen: Screen("CONVERSATION_SCREEN")
}