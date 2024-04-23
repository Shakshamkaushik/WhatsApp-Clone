package com.example.whatsappclone.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.R

class HelpActivity : AppCompatActivity() {
    private lateinit var helpRL : RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Help"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    theme
                )
            )
        )
        helpRL = findViewById(R.id.helpSettingsAppInfoRL)
        helpRL.setOnClickListener {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return true
    }
}