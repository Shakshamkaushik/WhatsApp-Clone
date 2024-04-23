package com.example.whatsappclone.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.whatsappclone.fragments.CallFragment
import com.example.whatsappclone.fragments.ChatFragment
import com.example.whatsappclone.fragments.StatusUpdatesFragment

class viewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ChatFragment()
            }
            1 -> {
                StatusUpdatesFragment()
            }
            else -> {
                CallFragment()
            }
        }


    }

}
