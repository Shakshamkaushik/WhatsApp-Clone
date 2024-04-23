package com.example.whatsappclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.models.CallDataModel
import com.example.whatsappclone.utils.CallDataUtils

class CallAdapter(val context: Context, val callData: ArrayList<CallDataModel>) : RecyclerView.Adapter<CallAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var profilePicture: ImageView = itemView.findViewById(R.id.callProfilePicture)
        var profileName: TextView = itemView.findViewById(R.id.callProfileName)
        var callImageView: ImageView = itemView.findViewById(R.id.callIconImage)
        var callTime: TextView = itemView.findViewById(R.id.callDateAndTime)
        var callDirectionIcon: ImageView = itemView.findViewById(R.id.callDirectionIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.call_rv_layout,parent,false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
       return callData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = callData[position]
        holder.profileName.text = data.getProfileName()
        holder.callTime.text = data.getCallDateAndTime()
        holder.profilePicture.setImageResource(data.getProfilePicture())
        holder.callDirectionIcon.setImageResource(CallDataUtils.getCallDirectionIcon(data))
        holder.callImageView.setOnClickListener {
            Toast.makeText(context, "503, Can't Place Call", Toast.LENGTH_SHORT).show()
        }
    }

}