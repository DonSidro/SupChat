package com.mad.hippo.codes.messaging.presentation.convervations.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mad.hippo.codes.messaging.presentation.convervations.ConversationsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ConversationTopBar(
    viewModel: ConversationsViewModel = getViewModel(),
    ) {

    var openMenu: Boolean by remember { mutableStateOf(false) }

    TopAppBar (
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OVERVIEW_SCREEN"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            openMenu = !openMenu
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = null,
                        )
                    }
                }
            }
        },
        actions = {
            DropdownMenu(
                expanded = openMenu,
                onDismissRequest = {
                    openMenu = !openMenu
                }
            ) {
                DropdownMenuItem(
                    onClick = {
                        //create chat here with
                        viewModel.onOpenDialogClicked()
                        openMenu = !openMenu
                    }
                ) {
                    Text(
                        text = "CREATE_CHAT"
                    )
                }
            }
        }
    )
    
    CreateChatDialog(viewModel = viewModel)

}

@Composable
fun CreateChatDialog(viewModel: ConversationsViewModel){

    val showDialogState: Boolean by viewModel.showDialog.collectAsState()
    var user by remember { mutableStateOf("") }

    if(showDialogState) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    Log.d("TAG", "CreateChatDialog: ")
                    viewModel.getOrCreateConversation(user)
                })
                { Text(text = "Create Chat") }
            },
            dismissButton = {
                TextButton(onClick = {viewModel.onDialogDismiss()})
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Please confirm") },
            text = { Column() {

            Text(text = "Create Chat with user $user")
                TextField(
                    value = user,
                    onValueChange = { user = it }
                )
            }}
        )
    }
}