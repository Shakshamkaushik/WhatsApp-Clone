package com.example.whatsappclone.utils

import android.util.Log
import io.grpc.android.BuildConfig

object GlobalLog {
    private val toShowLog = BuildConfig.DEBUG


    fun showLog(TAG : String, Msg: String) {
        Log.d(TAG,Msg+ " " + "[Thread: " + Thread.currentThread().name + "]")
    }

    fun logError(TAG: String?, MSG: String) {
        if (toShowLog && TAG != null) {
            Log.e(TAG, MSG + " " + "[Thread: " + Thread.currentThread().name + "]")
        }
    }
}