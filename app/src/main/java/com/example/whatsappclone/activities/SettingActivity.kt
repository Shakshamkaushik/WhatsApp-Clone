package com.example.whatsappclone.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.setMargins
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.whatsappclone.R
import com.example.whatsappclone.constants.SharedPrefrencesConstant
import com.example.whatsappclone.utils.SharedPrefrencesUtil
import com.google.firebase.auth.FirebaseAuth

class SettingActivity : AppCompatActivity() {
    private lateinit var settingToolbar: Toolbar
    private lateinit var userName: TextView
    private lateinit var userStatus: TextView
    private lateinit var userProfilePicture: ImageView
    private lateinit var qrCode: RelativeLayout
    private lateinit var userPersonalInfo: RelativeLayout
    private lateinit var mainSettingAccountContainer: RelativeLayout
    private lateinit var mainSettingChatContainer: RelativeLayout
    private lateinit var helpContainer: RelativeLayout
    private lateinit var mainSettingInviteFriendContainer: RelativeLayout

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        settingToolbar = findViewById(R.id.settingToolbar)

        setSupportActionBar(settingToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    theme
                )
            )
        )

        userName = findViewById(R.id.settingsPageProfileName)
        userStatus = findViewById(R.id.settingsPageStatus)
        userProfilePicture = findViewById(R.id.settingsPageProfilePicture)
        qrCode = findViewById(R.id.settingsPageQrCodeContainer)
        userPersonalInfo = findViewById(R.id.settingsPagePersonalInformationContainer)
        mainSettingAccountContainer = findViewById(R.id.mainSettingAccountContainer)
        mainSettingChatContainer = findViewById(R.id.mainSettingChatContainer)
        helpContainer = findViewById(R.id.mainSettingHelpContainer)
        mainSettingInviteFriendContainer = findViewById(R.id.mainSettingInviteFriendContainer)

        auth = FirebaseAuth.getInstance()

        qrCode.setOnClickListener {
            qrCodeClickHandler()
        }

        userPersonalInfo.setOnClickListener {
            startActivity(Intent(this, UpdateProfileInfoActivity::class.java))
        }

        mainSettingAccountContainer.setOnClickListener {
            startActivity(Intent(this, AccountSettingActivity::class.java))
        }

        mainSettingChatContainer.setOnClickListener {
            startActivity(Intent(this, ChatSettingActivity::class.java))
        }

        mainSettingInviteFriendContainer.setOnClickListener {
            inviteFriendClickHandler()
        }

        helpContainer.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateProfilePicture()
        userName.text = getString(
            R.string.registered_user_name,
            SharedPrefrencesUtil.getStringPrefrences(
                this,
                SharedPrefrencesConstant.REGISTERED_USER_NAME
            )
        )
        userStatus.text = SharedPrefrencesUtil.getStringPrefrences(
            this,
            SharedPrefrencesConstant.USER_PROFILE_STATUS
        )
    }

    @SuppressLint("CheckResult")
    private fun updateProfilePicture() {
        val userPicture = auth.currentUser?.photoUrl.toString()

        if (userPicture != "null") {
            userProfilePicture.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            userProfilePicture.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            userProfilePicture.imageTintMode = null
            val layoutParams = FrameLayout.LayoutParams(userProfilePicture.layoutParams)
            layoutParams.setMargins(0)
            userProfilePicture.layoutParams = layoutParams

            Glide.with(this).load(userPicture)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
                .into(userProfilePicture)


        }
    }

    private fun inviteFriendClickHandler() {
        val inviteIntent = Intent(Intent.ACTION_SEND)
        inviteIntent.putExtra(Intent.EXTRA_TEXT, "Hey Please Checkout the WhatsApp Clone")
        inviteIntent.type = "text/plain"
        val choserIntent = Intent.createChooser(inviteIntent, "Share App using..")
        startActivity(choserIntent)
    }

    private fun qrCodeClickHandler() {
        Toast.makeText(this, "This Feature is not available right now!", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}