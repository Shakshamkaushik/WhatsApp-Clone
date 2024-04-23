package com.example.whatsappclone.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R

class SendMessageAdapter(itemView: View): RecyclerView.ViewHolder(itemView) {

    val chatSendMessage: TextView = itemView.findViewById(R.id.tvChatMessageSend)
    val chatSendMessageTime: TextView = itemView.findViewById(R.id.chatMessageSendTime)
    val sendMessageStatusTick: ImageView = itemView.findViewById(R.id.ivSendMessageStatusTick)
}