package com.example.whatsappclone.utils

import com.example.whatsappclone.R
import com.example.whatsappclone.models.CallDataModel

class CallDataUtils {
    companion object {

        fun getCallData(): ArrayList<CallDataModel> {
            val callDataList = ArrayList<CallDataModel>()

            callDataList.add(
                CallDataModel(
                    "Rishav",
                    "20 December, 10:40 am",
                    R.mipmap.ic_launcher_round,
                    isReceived = false,
                    isReceivedAccepted = false,
                    isReceivedRejected = false,
                    isTransmitted = true,
                    isTransmittedAccepted = true,
                    isTransmittedRejected = false
                )
            )

            callDataList.add(
                CallDataModel(
                    "Ronney",
                    "19 December, 8:32 am",
                    R.drawable.user,
                    isReceived = false,
                    isReceivedAccepted = false,
                    isReceivedRejected = false,
                    isTransmitted = true,
                    isTransmittedAccepted = false,
                    isTransmittedRejected = true
                )
            )

            callDataList.add(
                CallDataModel(
                    "Rishav",
                    "18 December, 3:32 am",
                    R.mipmap.ic_launcher_round,
                    isReceived = false,
                    isReceivedAccepted = false,
                    isReceivedRejected = false,
                    isTransmitted = true,
                    isTransmittedAccepted = false,
                    isTransmittedRejected = true
                )
            )

            callDataList.add(
                CallDataModel(
                    "Buddy",
                    "18 December, 2:22 am",
                    R.drawable.java,
                    isReceived = false,
                    isReceivedAccepted = false,
                    isReceivedRejected = false,
                    isTransmitted = true,
                    isTransmittedAccepted = true,
                    isTransmittedRejected = false
                )
            )

            callDataList.add(
                CallDataModel(
                    "Shubham",
                    "17 December, 8:18 pm",
                    R.drawable.shubham,
                    isReceived = false,
                    isReceivedAccepted = false,
                    isReceivedRejected = false,
                    isTransmitted = true,
                    isTransmittedAccepted = true,
                    isTransmittedRejected = false
                )
            )

            callDataList.add(
                CallDataModel(
                    "Buddy & 3 others",
                    "14 December, 11:18 pm",
                    R.drawable.sachin_tendulkar,
                    isReceived = true,
                    isReceivedAccepted = true,
                    isReceivedRejected = false,
                    isTransmitted = false,
                    isTransmittedAccepted = false,
                    isTransmittedRejected = false
                )
            )

            callDataList.add(
                CallDataModel(
                    "Rishav",
                    "13 December, 5:37 am",
                    R.mipmap.ic_launcher_round,
                    isReceived = true,
                    isReceivedAccepted = false,
                    isReceivedRejected = true,
                    isTransmitted = false,
                    isTransmittedAccepted = false,
                    isTransmittedRejected = false
                )
            )

            callDataList.add(
                CallDataModel(
                    "Akay",
                    "7 December, 10:45 am",
                    R.drawable.user,
                    isReceived = false,
                    isReceivedAccepted = false,
                    isReceivedRejected = false,
                    isTransmitted = true,
                    isTransmittedAccepted = false,
                    isTransmittedRejected = true
                )
            )

            callDataList.add(
                CallDataModel(
                    "Rinki, Rishabh & 2 others",
                    "3 December, 7:28 pm",
                    R.drawable.user,
                    isReceived = false,
                    isReceivedAccepted = false,
                    isReceivedRejected = false,
                    isTransmitted = true,
                    isTransmittedAccepted = true,
                    isTransmittedRejected = false
                )
            )

            callDataList.add(
                CallDataModel(
                    "Punnet",
                    "18 November, 11:30 am",
                    R.drawable.user,
                    isReceived = true,
                    isReceivedAccepted = true,
                    isReceivedRejected = false,
                    isTransmitted = false,
                    isTransmittedAccepted = false,
                    isTransmittedRejected = false
                )
            )

            callDataList.add(
                CallDataModel(
                    "Shubham",
                    "29 October, 7:58 pm",
                    R.drawable.shubham,
                    isReceived = true,
                    isReceivedAccepted = true,
                    isReceivedRejected = false,
                    isTransmitted = false,
                    isTransmittedAccepted = false,
                    isTransmittedRejected = false
                )
            )

            callDataList.add(
                CallDataModel(
                    "Buddy",
                    "16 December, 8:10 am",
                    R.drawable.java,
                    isReceived = true,
                    isReceivedAccepted = false,
                    isReceivedRejected = false,
                    isTransmitted = false,
                    isTransmittedAccepted = false,
                    isTransmittedRejected = false
                )
            )

            callDataList.add(
                CallDataModel(
                    "Sachin  & 3 others",
                    "2 October, 6:23 pm",
                    R.drawable.sachin_tendulkar,
                    isReceived = false,
                    isReceivedAccepted = false,
                    isReceivedRejected = false,
                    isTransmitted = true,
                    isTransmittedAccepted = true,
                    isTransmittedRejected = false
                )
            )

            callDataList.add(
                CallDataModel(
                    "Pathak",
                    "13 September, 5:37 am",
                    R.drawable.user,
                    isReceived = false,
                    isReceivedAccepted = false,
                    isReceivedRejected = false,
                    isTransmitted = true,
                    isTransmittedAccepted = false,
                    isTransmittedRejected = true
                )
            )

            callDataList.add(
                CallDataModel(
                    "Aksh",
                    "7 September, 10:45 am",
                    R.drawable.user,
                    isReceived = false,
                    isReceivedAccepted = false,
                    isReceivedRejected = false,
                    isTransmitted = true,
                    isTransmittedAccepted = false,
                    isTransmittedRejected = true
                )
            )



            return callDataList
        }


        fun getCallDirectionIcon(data: CallDataModel): Int {
            // if we made a call
            return if (data.getIsTransmitted()) {
                if (data.getIsTransmittedAccepted()) {
                    R.drawable.transmitted_call_accepted
                } else {
                    R.drawable.transmitted_call_rejected
                }
            } else {
                if (data.getIsReceivedAccepted()) {
                    R.drawable.received_call_accepted
                } else {
                    R.drawable.received_call_rejected
                }
            }
        }
    }
}