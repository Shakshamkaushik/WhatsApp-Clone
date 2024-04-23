package com.example.whatsappclone.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.whatsappclone.R
import com.example.whatsappclone.utils.SharedPrefrencesUtil
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class AccountSettingActivity : AppCompatActivity() {
    lateinit var logOut: RelativeLayout
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Account"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color, theme
                )
            )
        )
        auth = FirebaseAuth.getInstance()
        logOut = findViewById(R.id.accountSettingLogOutRL)

        logOut.setOnClickListener { logoutHandler() }
    }

    private fun logoutHandler() {
        val alertDialog = AlertDialog.Builder(this).setTitle("Please confirm")
            .setMessage("Are you Sure you want to logout?").setPositiveButton("Yes") { _, _ ->
                clearAppCache()
                auth.signOut()
                ActivityCompat.finishAffinity(this)
                SharedPrefrencesUtil.clearAllSharedPrefrences(this)
                val mainWelcomeScreenActivityIntent =
                    Intent(this, HomeMainScreenActivity::class.java)
                startActivity(mainWelcomeScreenActivityIntent)
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.create()

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#4DAC9C"))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#4DAC9C"))
        }
        alertDialog.show()
    }


    private fun clearAppCache() {
        try {
            val dir: File = applicationContext.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteDir(dir: File): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            children.indices.forEach { i ->
                val sucess = deleteDir(File(dir, children[i]))
                if (!sucess) {
                    return false
                }

            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}