package com.mad.hippo.codes.messaging.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mad.hippo.codes.messaging.domain.model.*
import com.mad.hippo.codes.messaging.domain.repository.FirestoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class FirebaseRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FirestoreRepository {

    override fun initCurrentUserIfFirstTime(publicKey: String, onComplete: () -> Unit) {
        val docRef = firestore.collection("Users").document(auth.currentUser?.uid ?: "")
        docRef.get().addOnSuccessListener {
            if(!it.exists()){
                val newUser = User(uid = auth.currentUser?.uid ?: "",
                    name = auth.currentUser?.displayName ?: "",
                    publicKey = publicKey,
                    anonymous = auth.currentUser?.isAnonymous ?: false,
                    email = auth.currentUser?.email ?: "")
                docRef.set(newUser).addOnSuccessListener { onComplete() }
            }else{
                onComplete()
            }
        }
    }

    override fun  getOrCreateConversation(otherUserId: String) = callbackFlow {
        val currentUserRef = firestore.collection("Users").document(auth.currentUser?.uid ?: "")
        val conversationsRef = firestore.collection("Conversations")
            trySend(Response.Loading)
            currentUserRef.collection("engagedConversations").document(otherUserId).get()
                .addOnSuccessListener {
                    Log.d("TAG", "getOrCreateConversation: ")
                    if (it.exists()) {
                        Log.d("TAG", "getOrCreateConversation: 1")
                        trySend(Response.Success(it["ConversationID"].toString()))
                        return@addOnSuccessListener
                    }
                    val currentUserID = auth.currentUser?.uid ?: ""
                    val newConversation = conversationsRef.document()
                    newConversation.set(Conversation(newConversation.id, mutableListOf(currentUserID, otherUserId)))
                    Log.d("TAG", "getOrCreateConversation: 2")

                    currentUserRef.collection("engagedConversations")
                        .document(otherUserId)
                        .set(mapOf("ConversationID" to newConversation.id))
                    Log.d("TAG", "getOrCreateConversation: 3")

                    firestore.collection("Users").document(otherUserId)
                        .collection("engagedConversations")
                        .document(currentUserID)
                        .set(mapOf("ConversationID" to newConversation.id))
                    Log.d("TAG", "getOrCreateConversation: 4")

                    trySend(Response.Success(newConversation))

                }.await()
        awaitClose {  }
    }

    override fun getMessagesFromConversation(conversationID: String): Flow<Response<List<Message>>> {
        val conversationsRef = firestore.collection("Conversations")

        return callbackFlow { trySend(Response.Loading)
            Log.d("TAG", "getMessagesFromConversation: ")
            conversationsRef.document(conversationID).collection("messages").orderBy("time", Query.Direction.ASCENDING).addSnapshotListener{ querySnapshot, firebaseFirestoreExeption ->
                if(firebaseFirestoreExeption != null){
                    trySend(Response.Error(firebaseFirestoreExeption.message ?: ""))
                    return@addSnapshotListener
                }
                val messages = mutableListOf<Message>()
                querySnapshot?.documents?.forEach {
                    Log.d("TAG", "getMessagesFromConversation: 2")
                    var message = it.toObject(Message::class.java)
                    if(it["type"] == MessageType.TEXT){
                        message?.let { it1 -> messages.add(it1) }
                    }else{
                        message?.type ?: MessageType.IMAGE
                        message?.let { it1 -> messages.add(it1) }
                    }
                }
                trySend(Response.Success(messages))
            }

        awaitClose {  }
        }
    }

    override fun getConversationForUser() = callbackFlow  {
        val currentUserRef = firestore.collection("Users").document(auth.currentUser?.uid ?: "")
        trySend(Response.Loading)
        val list = arrayListOf<String>()
        currentUserRef.collection("engagedConversations").get().addOnSuccessListener {
            it.documents.forEach { doc ->
                Log.d("TAG", "getConversationForUser: ${doc["ConversationID"]}")
                list.add(doc["ConversationID"] as String)
            }
        }.await()

        var listOfConversation = mutableListOf<Conversation>()
        list.forEach {
            firestore.collection("Conversations").document(it).get().addOnSuccessListener { doc ->
                val variable = doc.get("userIDs") as MutableList<String>
                listOfConversation.add(Conversation(it,variable))
            }.await()
        }

        trySend(Response.Success(listOfConversation))

        awaitClose {  }
    }

    override fun sendMessage(message: Message, conversationID: String): Flow<Response<Boolean>> {
        val conversationsRef = firestore.collection("Conversations")
        return callbackFlow {
            conversationsRef.document(conversationID).collection("messages")
                .add(message)
            awaitClose {  }
        }


    }

    override fun getOtherUser(otherUserId: String): Flow<User> {
        val usersRef = firestore.collection("Users")
        Log.d("TAG", "getOtherUser: $otherUserId")
        return callbackFlow {
            usersRef.document(otherUserId).addSnapshotListener { value, error ->
                if(error != null){
                    trySend(User())
                }
                value?.toObject(User::class.java)?.let { trySend(it) }
            }
            awaitClose {  }
        }
    }


}