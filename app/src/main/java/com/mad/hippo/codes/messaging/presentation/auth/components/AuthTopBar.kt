package com.mad.hippo.codes.messaging.presentation.auth.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun AuthTopBar() {
    TopAppBar (
        title = {
            Text(
                text = "AUTH_SCREEN"
            )
        }
    )
}