package com.mad.hippo.codes.messaging.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mad.hippo.codes.messaging.presentation.profile.ProfileViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.compose.getViewModel

@Composable
@InternalCoroutinesApi
fun ProfileTopBar(
    viewModel: ProfileViewModel = getViewModel(),
) {
    var openMenu: Boolean by remember { mutableStateOf(false) }

    TopAppBar (
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PROFILE_SCREEN"
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
                        viewModel.onOpenDialogClicked()
                        openMenu = !openMenu
                    }
                ) {
                    Text(
                        text = "SIGN_OUT"
                    )
                }
            }
        }
    )
    
    signOutDialog(viewModel = viewModel)
}

@Composable
fun signOutDialog(viewModel: ProfileViewModel){

    val showDialogState: Boolean by viewModel.showDialog.collectAsState()

    if(showDialogState) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.signOut()
                    viewModel.onDialogDismiss()
                })
                { Text(text = "Sign Out") }
            },
            dismissButton = {
                TextButton(onClick = {viewModel.onDialogDismiss()})
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Please confirm") },
            text = { Text(text = "You will not be able to restore this Anonymous account, and all chats will be lost!") }
        )
    }
}