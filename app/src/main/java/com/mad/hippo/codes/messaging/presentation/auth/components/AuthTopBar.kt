package com.mad.hippo.codes.messaging.presentation.auth.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mad.hippo.codes.messaging.R

@Composable
fun AuthTopBar() {
    TopAppBar (
        title = {
            Text(
                text = stringResource(R.string.auth_screen_title)
            )
        }
    )
}