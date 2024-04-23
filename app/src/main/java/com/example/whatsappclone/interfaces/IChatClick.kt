package com.example.whatsappclone.interfaces

import com.example.whatsappclone.models.firebase.User

interface IChatClick {

    fun onItemClicked(user: User?)
}