package com.mad.hippo.codes.messaging.presentation.profile.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mad.hippo.codes.messaging.presentation.profile.ProfileViewModel
import com.mad.hippo.codes.messaging.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

private const val TAG = "ProfileContent"
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ProfileContent(uid : String
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = "Anonymous User : $uid",
                fontSize = 24.sp
            )
        }


    }
}