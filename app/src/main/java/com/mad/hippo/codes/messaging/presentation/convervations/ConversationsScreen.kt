package com.mad.hippo.codes.messaging.presentation.convervations

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.mad.hippo.codes.messaging.domain.model.Conversation
import com.mad.hippo.codes.messaging.utils.Response
import com.mad.hippo.codes.messaging.presentation.convervations.components.ConversationItem
import com.mad.hippo.codes.messaging.presentation.convervations.components.ConversationTopBar
import com.mad.hippo.codes.messaging.presentation.navigation.Screen
import org.koin.androidx.compose.getViewModel

private const val TAG = "OverviewScreen"
@Composable
fun ConversationsScreen (
    navController: NavController,
    viewModel: ConversationsViewModel = getViewModel()
){

    var myList : List<Conversation?> by remember { mutableStateOf(listOf())}
    val state by viewModel.list.collectAsState()

    LaunchedEffect(true){
        viewModel.getConversationList()

    }
    Scaffold(
        topBar = {
            ConversationTopBar()
        }
    ) {
        
        LazyColumn{
            items(items = myList){ item ->
                item?.let { it1 -> ConversationItem(conversation = it1){
                    var list = it1.userIDs.joinToString(separator = ",")
                    navController.navigate(Screen.ChatScreen.route + "/${it1.iD}"+"/${list}")
                } }
            }
        }

    }


    when(state) {
        is Response.Error -> {
        }
        Response.IDLE -> {}
        Response.Loading -> {
            CircularProgressIndicator()}
        is Response.Success -> {
            Log.d(TAG, "OverviewScreen: ")
                myList = (state as Response.Success<List<Conversation>>).data

            }
        }


}
fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}
