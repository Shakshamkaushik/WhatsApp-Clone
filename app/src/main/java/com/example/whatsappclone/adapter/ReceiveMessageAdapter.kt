package com.example.whatsappclone.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R

class ReceiveMessageAdapter(itemView: View): RecyclerView.ViewHolder(itemView) {

    val chatReceivedMessage: TextView = itemView.findViewById(R.id.chatMessageReceived)
    val chatReceivedMessageTime: TextView = itemView.findViewById(R.id.chatMessageReceivedTime)
}