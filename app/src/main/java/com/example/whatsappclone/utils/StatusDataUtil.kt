package com.example.whatsappclone.utils

import com.example.whatsappclone.R
import com.example.whatsappclone.models.StatusData

class StatusDataUtil {
    companion object {

        fun getRecentStatusData(): ArrayList<StatusData> {
            val recentStatusDataList = ArrayList<StatusData>()

            recentStatusDataList.add(
                StatusData(
                    "Java ❤️",
                    "Just Now",
                    R.drawable.java
                )
            )

            recentStatusDataList.add(
                StatusData(
                    "Nikhil",
                    "Today, 10:35 pm",
                    R.mipmap.ic_launcher_round
                )
            )

            recentStatusDataList.add(
                StatusData(
                    "Ronney",
                    "Today, 3:35 pm",
                    R.drawable.user
                )
            )

            recentStatusDataList.add(
                StatusData(
                    "Shubham",
                    "Today, 5:10 am",
                    R.drawable.shubham
                )
            )

            recentStatusDataList.add(
                StatusData(
                    "Akshay",
                    "Today, 5:08 am",
                    R.drawable.user
                )
            )


            recentStatusDataList.add(
                StatusData(
                    "Paras",
                    "Yesterday, 9:59 pm",
                    R.drawable.user
                )
            )

            return recentStatusDataList
        }

    }
}