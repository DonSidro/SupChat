package com.mad.hippo.codes.messaging.domain.use_case

data class UseCases(
    val signInAnonymously: SignInAnonymously,
    val signInWithGoogle: SignInWithGoogle,
    val signOut: SignOut,
    var createUserOnFireStore: CreateUserOnFireStore,
    val isUserAuthenticated: IsUserAuthenticated,
    val getUID: GetUID,
    val getUserConversation: GetUserConversation,
    val createConversation: CreateConversation,
    val getConversationMessages: GetConversationMessages,
    val sendMessage: SendMessage,
    val getCurrentUser: GetCurrentUser,
    val getOtherUser: GetOtherUser
)