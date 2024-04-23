package com.example.whatsappclone.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.R

class AppInfoActivity : AppCompatActivity() {

    private lateinit var license: TextView
    private lateinit var appVersion: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)


    }
}