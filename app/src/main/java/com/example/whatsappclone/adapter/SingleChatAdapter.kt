package com.example.whatsappclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.constants.ApplicationConstant
import com.example.whatsappclone.models.firebase.ChatMessage
import com.example.whatsappclone.utils.SecurityUtils
import com.google.firebase.auth.FirebaseAuth

class SingleChatAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatMessageData: ArrayList<ChatMessage> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ApplicationConstant.MESSAGE_SEND -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.chat_message_sender_layout, parent, false)

                SendMessageAdapter(view)
            }

            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.chat_message_received_layout, parent, false)

                ReceiveMessageAdapter(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return chatMessageData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatMessage = chatMessageData[position]

        if (holder.itemViewType == ApplicationConstant.MESSAGE_SEND) {

            val sendMessageAdapter = holder as SendMessageAdapter
            sendMessageAdapter.chatSendMessage.text = SecurityUtils.decryptMessage(
                chatMessage.getMessage(),
                chatMessage.getSenderUid(),
                chatMessage.getReceiverUid()
            )
            holder.chatSendMessageTime.text = chatMessage.getChatMessageTime().toString()

            when {
                chatMessage.getIsMessageSeen() -> {
                    Glide.with(context).load(R.drawable.blue_tick)
                        .into(holder.sendMessageStatusTick)
                }

                chatMessage.getIsMessageDelivered() -> {
                    Glide.with(context).load(R.drawable.double_tick)
                        .into(holder.sendMessageStatusTick)
                }

                else -> {
                    Glide.with(context).load(R.drawable.single_tick)
                        .into(holder.sendMessageStatusTick)
                }
            }
        } else if (holder.itemViewType == ApplicationConstant.MESSAGE_RECEIVED) {
            val receiveMessageAdapter = holder as ReceiveMessageAdapter
            receiveMessageAdapter.chatReceivedMessage.text = SecurityUtils.decryptMessage(
                chatMessage.getMessage(),
                chatMessage.getSenderUid(),
                chatMessage.getReceiverUid()
            )
            holder.chatReceivedMessageTime.text = chatMessage.getChatMessageTime().toString()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chatMessage = chatMessageData[position]

        return if (chatMessage.getSenderUid() == FirebaseAuth.getInstance().currentUser?.uid) {
            ApplicationConstant.MESSAGE_SEND
        } else {
            ApplicationConstant.MESSAGE_RECEIVED
        }
    }

    fun updateChatMessageDataset(chats: ArrayList<ChatMessage>) {
        this.chatMessageData = chats
    }
}