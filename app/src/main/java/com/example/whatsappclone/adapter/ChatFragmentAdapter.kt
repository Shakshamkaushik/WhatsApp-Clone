package com.example.whatsappclone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.interfaces.IChatClick
import com.example.whatsappclone.models.firebase.User

class ChatFragmentAdapter(val context: Context, private val chatClickListener: IChatClick) :
    RecyclerView.Adapter<ChatAdapter>() {
    private var chatUsersData: ArrayList<User> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter {

        val view =
            LayoutInflater.from(context).inflate(R.layout.chat_message_rv_item, parent, false)
        val viewHolder = ChatAdapter(view)

        view.setOnClickListener {
            chatClickListener.onItemClicked(chatUsersData[viewHolder.adapterPosition])
        }


        return viewHolder
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChatAdapter, position: Int) {
        val user = chatUsersData[position]
        holder.chatTitle.text = user.getName()
        holder.chatMessagePreview.text = "${user.getMobileNumber()} "
        holder.lastSeenTime.text = "Tap to start chatting"

if (user.getProfilePictureUrl() != null && user.getProfilePictureUrl() != "null") {
    holder.imageView.layoutParams.width = LayoutParams.MATCH_PARENT
    holder.imageView.layoutParams.height = LayoutParams.MATCH_PARENT
    holder.imageView.scaleType = ImageView.ScaleType.CENTER

    holder.imageView.imageTintMode = null
    Glide.with(holder.itemView).load(user.getProfilePictureUrl())
        .into(holder.imageView)

}

    }


    override fun getItemCount(): Int {

        return chatUsersData.size
    }


    fun updateChatData(chatUsersData: ArrayList<User>) {

        this.chatUsersData = chatUsersData
    }


}