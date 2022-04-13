package com.mad.hippo.codes.messaging.presentation.conversation

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
import com.mad.hippo.codes.messaging.domain.model.Message
import com.mad.hippo.codes.messaging.presentation.convervations.ConversationsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun OverviewTopBar(
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
                    text = "Conversation_SCREEN"
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
                        openMenu = !openMenu
                    }
                ) {
                    Text(
                        text = "TEST_CHAT"
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
                    viewModel.sendMessage(Message("Hej Jnr & Discord", "10/10", "Qt2fiAsRGZVOCtAtewY0", "TEXT"), "PSUDePdSzeH5Ti1nG8TK")
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
            }
            }
        )
    }
}