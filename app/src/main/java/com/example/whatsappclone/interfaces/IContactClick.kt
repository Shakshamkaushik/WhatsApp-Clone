package com.example.whatsappclone.interfaces

import com.example.whatsappclone.models.firebase.User

interface IContactClick {

    fun onItemClicked(user: User?)
}