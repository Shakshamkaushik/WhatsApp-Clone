package com.example.whatsappclone.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.R
import com.google.firebase.auth.FirebaseAuth

class WelcomeAgreeAndContinueActivity : AppCompatActivity() {
    private lateinit var btn: Button
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_agree_and_continue)
        btn = findViewById(R.id.btnAgreeAndContinue)
        auth = FirebaseAuth.getInstance()

        btn.setOnClickListener {
            startActivity(
                Intent(
                    this@WelcomeAgreeAndContinueActivity,
                    EnterPhoneNumberActivity::class.java
                )
            )
            finish()
        }

        if (auth.currentUser != null) {
            startActivity(
                Intent(
                    this@WelcomeAgreeAndContinueActivity,
                    HomeMainScreenActivity::class.java
                )
            )
            finish()
        }
    }
}