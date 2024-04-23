package com.example.whatsappclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.models.StatusData

class ViewStatusAdapter ( val context: Context,
private val viewedStatusData: ArrayList<StatusData>
) : RecyclerView.Adapter<ViewStatusAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var statusUserName: TextView = itemView.findViewById(R.id.statusUploaderUserName)
        var statusUploadTime: TextView = itemView.findViewById(R.id.statusUploadTime)
        var profilePicture: ImageView = itemView.findViewById(R.id.statusProfilePicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_status, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return viewedStatusData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = viewedStatusData[position]
        holder.statusUserName.text = data.getStatusTitle()
        holder.statusUploadTime.text = data.getStatusTime()
        holder.profilePicture.setImageResource(data.getStatusProfilePicture())
    }
}