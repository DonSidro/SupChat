package com.mad.hippo.codes.messaging.domain.model

data class Message(
    var textSender: String = "",
    var textReceiver: String = "",
    val time: String = "",
    val senderId: String = "",
    var type: String = MessageType.TEXT,
    var seen : Boolean = false,
    var image : String = ""
)
object MessageType{
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}
