package com.mad.hippo.codes.messaging.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mad.hippo.codes.messaging.R
import com.mad.hippo.codes.messaging.data.repository.AuthRepositoryImpl
import com.mad.hippo.codes.messaging.data.repository.FirebaseRepositoryImpl
import com.mad.hippo.codes.messaging.domain.repository.AuthRepository
import com.mad.hippo.codes.messaging.domain.repository.FirestoreRepository
import com.mad.hippo.codes.messaging.domain.use_case.*
import com.mad.hippo.codes.messaging.presentation.MainViewModel
import com.mad.hippo.codes.messaging.presentation.auth.AuthViewModel
import com.mad.hippo.codes.messaging.presentation.convervations.ConversationsViewModel
import com.mad.hippo.codes.messaging.presentation.profile.ProfileViewModel
import com.mad.hippo.codes.messaging.utils.DataStoreManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val firebaseRepositoryModule = module {

    fun provideRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }
    fun provideFireStoreRepository(firestore: FirebaseFirestore, auth: FirebaseAuth) : FirestoreRepository{
        return FirebaseRepositoryImpl(firestore = firestore, auth = auth)
    }
    single { provideRepository(get()) }
    single { provideFireStoreRepository(get(), get()) }

}



val authViewModelModule = module {
    viewModel {
        AuthViewModel(useCases = get())
    }
}

val profileViewModelModule = module {
    viewModel {
        ProfileViewModel(useCases = get())
    }
}

val mainViewModelModule = module {
    viewModel {
        MainViewModel(useCases = get())
    }
}

val overviewViewModelModule = module {
    viewModel {
        ConversationsViewModel(useCases = get())
    }
}

val useCaseModule = module {

    fun provideSignInAnonymously(repository: AuthRepository) : SignInAnonymously{
        return SignInAnonymously(repository = repository)
    }
    fun provideSignInWithGoogle(repository: AuthRepository) : SignInWithGoogle{
        return SignInWithGoogle(repository = repository)
    }
    fun provideSignOut(repository: AuthRepository) : SignOut{
        return SignOut(repository = repository)
    }
    fun provideCreateUser(repository: FirestoreRepository) : CreateUserOnFireStore{
        return CreateUserOnFireStore(repository = repository)
    }
    fun provideIsUserAuthenticated(authRepository: AuthRepository) : IsUserAuthenticated{
        return IsUserAuthenticated(repository = authRepository)
    }
    fun provideGetUID(authRepository: AuthRepository) : GetUID{
        return GetUID(repository = authRepository)
    }
    fun provideGetCurrentUser(authRepository: AuthRepository) : GetCurrentUser{
        return GetCurrentUser(repository = authRepository)
    }
    fun provideGetOtherUser(repository: FirestoreRepository) : GetOtherUser{
        return GetOtherUser(repository = repository)
    }
    fun provideGetConversations(repository: FirestoreRepository) : GetUserConversation{
        return GetUserConversation(repository = repository)
    }
    fun provideGetOrCreateConversation(repository: FirestoreRepository) : CreateConversation{
        return CreateConversation(repository = repository)
    }
    fun provideGetConversationMessages(repository: FirestoreRepository) : GetConversationMessages{
        return GetConversationMessages(repository = repository)
    }

    fun provideSendMessage(repository: FirestoreRepository) : SendMessage{
        return SendMessage(repository = repository)
    }

    fun provideUseCases(
                        signInAnonymously: SignInAnonymously,
                        signOut: SignOut,
                        createUserOnFireStore: CreateUserOnFireStore,
                        signInWithGoogle: SignInWithGoogle,
                        isUserAuthenticated: IsUserAuthenticated,
                        getUID: GetUID,
                        getUserConversation: GetUserConversation,
                        createConversation: CreateConversation,
                        getConversationMessages: GetConversationMessages,
                        sendMessage: SendMessage,
                        getCurrentUser: GetCurrentUser,
                        getOtherUser: GetOtherUser) : UseCases{
        return UseCases(
        signInAnonymously = signInAnonymously,
        signOut = signOut,
            createUserOnFireStore = createUserOnFireStore,
            signInWithGoogle = signInWithGoogle,
            isUserAuthenticated = isUserAuthenticated,
            getUID = getUID ,
            getUserConversation = getUserConversation,
            createConversation = createConversation,
            getConversationMessages = getConversationMessages,
            sendMessage = sendMessage,
            getCurrentUser = getCurrentUser,
            getOtherUser = getOtherUser
        )
    }

    single { provideSignInAnonymously(get()) }
    single { provideSignOut(get()) }
    single { provideCreateUser(get()) }
    single { provideSignInWithGoogle(get()) }
    single { provideIsUserAuthenticated(get()) }
    single { provideGetOrCreateConversation(get()) }
    single { provideGetUID(get()) }
    single { provideGetCurrentUser(get()) }
    single { provideGetOtherUser(get()) }
    single { provideGetConversations(get()) }
    single { provideSendMessage(get()) }
    single { provideGetConversationMessages(get()) }
    single { provideUseCases(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { DataStoreManager(get()) }
}


val firebaseModule = module {

    fun provideGso(context: Context) : GoogleSignInOptions {
        return  GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.client_id))
            .requestEmail()
            .build()
    }

    fun provideGoogleClient(context: Context, gso: GoogleSignInOptions): GoogleSignInClient{
        return GoogleSignIn.getClient(context, gso)
    }



    single { FirebaseAuth.getInstance() }

    single { FirebaseFirestore.getInstance() }

    single { provideGso(androidContext()) }

    single { provideGoogleClient(androidContext(), get()) }

}
