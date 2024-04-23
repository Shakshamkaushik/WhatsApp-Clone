package com.example.whatsappclone.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R

class ChatAdapter(itemView: View): RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.ivUserProfilePicture)
    val chatTitle: TextView = itemView.findViewById(R.id.tvChatTitle)
    val lastSeenTime: TextView = itemView.findViewById(R.id.tvLastSeenTime)
    val messageStatusTick: ImageView = itemView.findViewById(R.id.ivMessageStatusTick)
    val chatMessagePreview: TextView = itemView.findViewById(R.id.tvChatMessagePreview)
}