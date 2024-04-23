package com.example.whatsappclone.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.constants.ApplicationConstant

class FullScreenProfilePictureActivity : AppCompatActivity() {
    private lateinit var fullScreenProfilePicture: ImageView
    private lateinit var fullScreenToolBar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_profile_picture)

        fullScreenProfilePicture = findViewById(R.id.fullScreenProfilePictureImageView)
        fullScreenToolBar = findViewById(R.id.fullScreenToolBar)
        val title = intent.getStringExtra(ApplicationConstant.FULL_SCREEN_PROFILE_PICTURE_TITLE)
        val pictureUrl = intent.getStringExtra(ApplicationConstant.FULL_SCREEN_PROFILE_PICTURE_URL)
        val pictureUrlInt =
            intent.getIntExtra(ApplicationConstant.FULL_SCREEN_PROFILE_PICTURE_URL, 0)

        if (pictureUrl != null) {
            updateProfilePicture(pictureUrl)
        } else {
            updateProfilePicture(pictureUrlInt)
        }

        updateActionBar(title!!)
    }

    private fun updateProfilePicture(pictureUrl: Any) {
        Glide.with(this).load(pictureUrl).into(fullScreenProfilePicture)
    }

    private fun updateActionBar(title: String) {
        setSupportActionBar(fullScreenToolBar)
        supportActionBar?.title = title

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.black,
                    theme
                )
            )
        )
        window.statusBarColor = Color.BLACK
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            Toast.makeText(this, "Not Available Right Now", Toast.LENGTH_SHORT).show()
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.full_screen_items, menu)
        return true
    }
}