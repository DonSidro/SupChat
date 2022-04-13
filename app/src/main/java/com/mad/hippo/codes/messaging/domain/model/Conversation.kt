package com.mad.hippo.codes.messaging.domain.model

data class Conversation(
    val iD : String = "",
    val userIDs: MutableList<String> = mutableListOf()
)
