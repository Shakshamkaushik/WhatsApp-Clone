package com.example.whatsappclone.models.firebase

class ChatMessage {
    private lateinit var message: String
    private lateinit var chatMessageTime: ChatMessageTime
    private var time: Long = 0
    private var senderUid: String? = null
    private var receiverUid: String? = null
    private var isMessageDelivered: Boolean = false
    private var isMessageSeen: Boolean = false

    constructor()
    constructor(
        message: String,
        chatMessageTime: ChatMessageTime,
        time: Long,
        senderUid: String?,
        receiverUid: String?,
        isMessageDelivered: Boolean,
        isMessageSeen: Boolean
    ) {
        this.message = message
        this.chatMessageTime = chatMessageTime
        this.time = time
        this.senderUid = senderUid
        this.receiverUid = receiverUid
        this.isMessageDelivered = isMessageDelivered
        this.isMessageSeen = isMessageSeen
    }


    fun getMessage() = this.message

    fun getChatMessageTime() = this.chatMessageTime

    fun getTime() = this.time

    fun getSenderUid() = this.senderUid

    fun getReceiverUid() = this.receiverUid

    fun getIsMessageDelivered() = this.isMessageDelivered

    fun getIsMessageSeen() = this.isMessageSeen

    override fun toString(): String {
        return "$message send by $senderUid to $receiverUid @ $time"
    }

}