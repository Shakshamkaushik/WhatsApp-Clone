package com.example.whatsappclone.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.constants.ApplicationConstant
import com.example.whatsappclone.constants.ChatConstants

class ChatProfileInfoActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var backToChatButton: LinearLayout
    private lateinit var userProfileProfile: ImageView
    private lateinit var backArrow: ImageView
    private lateinit var userName: TextView
    private lateinit var userNumber: TextView
    private lateinit var userWhatsAppStatus: TextView
    private lateinit var userWhatsAppStatusDate: TextView
    private lateinit var userBlock: TextView
    private lateinit var userReport: TextView

    private var userDataBundle: Bundle? = null
    private var name: String? = null
    private var profilePicture: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_profile_info)


        toolbar = findViewById(R.id.chatProfileInfoToolBar)
        backToChatButton = findViewById(R.id.backToMainScreenLL)
        userProfileProfile = findViewById(R.id.ivchatProfileInfoImage)
        backArrow = findViewById(R.id.ivProfileInfoBackArrow)
        userName = findViewById(R.id.tvChatInfoUserName)
        userNumber = findViewById(R.id.tvChatProfileInfoUserNumber)
        userWhatsAppStatus = findViewById(R.id.tvChatProfileInfoWhatsAppStatus)
        userWhatsAppStatusDate = findViewById(R.id.tvChatProfileInfoWhatsAppStatusDate)
        userBlock = findViewById(R.id.tvChatProfileInfoBlock)
        userReport = findViewById(R.id.tvChatProfileInfoReport)

        userDataBundle = intent.extras
        updateToolBar()
        userProfileProfile.setOnClickListener { chatProfileImageClickHandler() }
        backArrow.setOnClickListener {
            startActivity(Intent(this@ChatProfileInfoActivity, HomeMainScreenActivity::class.java))
            finish()
        }
    }

    private fun chatProfileImageClickHandler() {
        val userImage = userDataBundle?.getString(ChatConstants.CHAT_PROFILE_PICTURE)

        if (userImage != null && userImage != "null") {
            val fullScreenIntent =
                Intent(this@ChatProfileInfoActivity, FullScreenProfilePictureActivity::class.java)
            fullScreenIntent.putExtra(ApplicationConstant.FULL_SCREEN_PROFILE_PICTURE_TITLE, name)
            fullScreenIntent.putExtra(
                ApplicationConstant.FULL_SCREEN_PROFILE_PICTURE_URL,
                userImage
            )
            startActivity(fullScreenIntent)
        } else {
            Toast.makeText(this, "no Profile Picture", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onResume() {
        super.onResume()
        name = userDataBundle?.getString(ChatConstants.CHAT_PROFILE_NAME)
        profilePicture = userDataBundle?.getString(ChatConstants.CHAT_PROFILE_PICTURE)
        userName.text = name
        userNumber.text = userDataBundle?.getString(ChatConstants.CHAT_PROFILE_PHONE_NUMBER)
        userWhatsAppStatus.text = userDataBundle?.getString(ChatConstants.CHAT_PROFILE_STATUS)
        userWhatsAppStatusDate.text =
            userDataBundle?.getString(ChatConstants.CHAT_PROFILE_STATUS_UPDATE_ON)
        userBlock.text = getString(R.string.block_user_text, name)
        userReport.text = getString(R.string.block_user_text, name)
        if (profilePicture != null && profilePicture != "null") {
            Glide.with(this).load(profilePicture).into(userProfileProfile)
        }

    }


    private fun updateToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayUseLogoEnabled(false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_profile_info_items, menu)
        return true
    }


}