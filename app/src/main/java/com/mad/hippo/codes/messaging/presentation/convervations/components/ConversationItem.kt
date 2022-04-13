package com.mad.hippo.codes.messaging.presentation.convervations.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mad.hippo.codes.messaging.domain.model.Conversation
import com.mad.hippo.codes.messaging.R

@Composable
fun ConversationItem (conversation: Conversation, onClick: () -> Unit){
    Row( // 1
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(60.dp).clickable { onClick.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "" , modifier = Modifier
                .size(45.dp)
                .clip(CircleShape))
        Column(modifier = Modifier
            .padding(start = 16.dp)
            .weight(1f)) { // 3
            Text(
                text = "${conversation.iD}• 23min ago",
                color = colorResource(id = R.color.black),
                style = TextStyle(fontWeight = FontWeight.ExtraLight),
                fontSize = 14.sp,
            )

            Row() {
                Text(
                    text = "lastMessageText",
                    maxLines = 1,
                    color = colorResource(id = R.color.black),
                    style = TextStyle(fontWeight = if(true) FontWeight.Bold else FontWeight.Light),
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )
                 Icon(modifier = Modifier
                     .size(16.dp)
                     .align(Alignment.Bottom)
                     .padding(start = 4.dp),imageVector = Icons.Default.Done,
                    tint = colorResource(id = R.color.black), contentDescription = "")
            }

        }


    }
}