package com.mad.hippo.codes.messaging.domain.model

data class User(
    var uid: String = "",
    var name : String = "",
    var email : String = "",
    var authenticated : Boolean = false,
    var anonymous : Boolean = false,
    var publicKey : String = ""
)
