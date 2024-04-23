package com.example.whatsappclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.interfaces.IContactClick
import com.example.whatsappclone.models.firebase.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject

class AllContactAdapter(
    private val context: Context,
    private val contactClickListener: IContactClick
) : RecyclerView.Adapter<AllContactAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePictureImage: ImageView = itemView.findViewById(R.id.ivUserProfilePicture)
        val contactName: TextView = itemView.findViewById(R.id.tvContactName)
        val contactStatus: TextView = itemView.findViewById(R.id.tvContactStatus)
    }

    private var contactsData = mutableListOf<DocumentSnapshot>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.all_contact_rv_layout, parent, false)
        val viewHolder = MyViewHolder(view)

        view.setOnClickListener {
            val user = contactsData[viewHolder.adapterPosition].toObject<User>()
            contactClickListener.onItemClicked(user)
        }

        return viewHolder
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = contactsData[position].toObject<User>()

        if (data != null) {
            holder.contactName.text = data.getName()
            holder.contactStatus.text = data.getProfileStatus()
            if (data.getProfilePictureUrl() != null && data.getProfilePictureUrl() != "null") {
                holder.profilePictureImage.layoutParams.width =
                    ViewGroup.LayoutParams.MATCH_PARENT
                holder.profilePictureImage.layoutParams.height =
                    ViewGroup.LayoutParams.MATCH_PARENT
                holder.profilePictureImage.scaleType = ImageView.ScaleType.FIT_XY
                holder.profilePictureImage.imageTintMode = null
                Glide.with(holder.itemView).load(data.getProfilePictureUrl())
                    .into(holder.profilePictureImage)
            }
        }
    }


    override fun getItemCount(): Int {
        return contactsData.size
    }


    fun updateContactDataSet(contactsData: MutableList<DocumentSnapshot>) {
        this.contactsData = contactsData
    }

}