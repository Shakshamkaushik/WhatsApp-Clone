package com.example.whatsappclone.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.forEach
import com.example.whatsappclone.R
import com.example.whatsappclone.constants.ApplicationConstant
import com.example.whatsappclone.constants.ApplicationLoggingConstant
import com.example.whatsappclone.constants.SharedPrefrencesConstant
import com.example.whatsappclone.utils.SharedPrefrencesUtil
import java.util.Locale

class ChatSettingActivity : AppCompatActivity() {

    private lateinit var theme: RelativeLayout
    private lateinit var enterKeySendRL: RelativeLayout
    private lateinit var mediaVisibilityRL: RelativeLayout
    private lateinit var keepChatsArchiveRL: RelativeLayout
    private lateinit var fontSizeRL: RelativeLayout
    private lateinit var appLanguageRL: RelativeLayout

    private lateinit var currentTheme: TextView
    private lateinit var currentChatFontSize: TextView
    private lateinit var chatSettingAppLanguage: TextView

    private lateinit var enterKeySendSwitch: SwitchCompat
    private lateinit var mediaVisibilitySwitch: SwitchCompat
    private lateinit var keepChatsArchiveSwitch: SwitchCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_setting)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chats"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    getTheme()
                )
            )
        )

        theme = findViewById(R.id.chatSettingsThemeRL)
        enterKeySendRL = findViewById(R.id.chatSettingsEnterKeySendRL)
        mediaVisibilityRL = findViewById(R.id.chatSettingsMediaVisibilityRL)
        keepChatsArchiveRL = findViewById(R.id.chatSettingsKeepChatsArchiveRL)
        fontSizeRL = findViewById(R.id.chatSettingsFontSizeRL)
        appLanguageRL = findViewById(R.id.chatSettingsAppLanguageRL)

        currentTheme = findViewById(R.id.tvCurrentTheme)
        currentChatFontSize = findViewById(R.id.tvCurrentChatFontSize)
        chatSettingAppLanguage = findViewById(R.id.tvChatSettingAppLanguage)

        enterKeySendSwitch = findViewById(R.id.enterKeyIsSendSwitch)
        mediaVisibilitySwitch = findViewById(R.id.mediaVisibilitySwitch)
        keepChatsArchiveSwitch = findViewById(R.id.keepChatsArchiveSwitch)


        theme.setOnClickListener { themeClickHandler() }
        enterKeySendRL.setOnClickListener { enterKeySendClickHandler() }
        mediaVisibilityRL.setOnClickListener { mediaVisibilityClickHandler() }
        keepChatsArchiveRL.setOnClickListener { keepChatsArchiveClickHandler() }
        fontSizeRL.setOnClickListener { fontSizeClickHandler() }
        appLanguageRL.setOnClickListener { appLanguageClickHandler() }

        enterKeySendSwitch.setOnClickListener { enterKeySendSwitchClickHandler() }
        mediaVisibilitySwitch.setOnClickListener { mediaVisibilitySwitchClickHandler() }
        keepChatsArchiveSwitch.setOnClickListener { keepChatsArchiveSwitchClickHandler() }
    }

    override fun onResume() {
        super.onResume()
        chatSettingAppLanguage.text =
            getString(R.string.lan_device, Locale.getDefault().displayLanguage)
        updateCurrentThemeText()
        updateAllSwitches()
        updateCurrentChatFontSizeText()
        updateCurrentAppLanguageText()
    }

    private fun updateCurrentAppLanguageText() {
        val currentAppLanguage = SharedPrefrencesUtil.getStringPrefrences(this,
        SharedPrefrencesConstant.CURRENT_APP_LANGUAGE
        )

        if (currentAppLanguage != null) {
            chatSettingAppLanguage.text = currentAppLanguage
        } else {
            println(Locale.getDefault().displayLanguage)
            chatSettingAppLanguage.text =
                getString(R.string.lan_device, Locale.getDefault().displayLanguage)
        }
    }

    private fun updateAllSwitches() {
        val enterSend = SharedPrefrencesUtil.getBooleanPrefrences(
            this,
            SharedPrefrencesConstant.ENTER_KEY_SEND
        )
        val mediaVisibility = SharedPrefrencesUtil.getBooleanPrefrences(
            this,
            SharedPrefrencesConstant.MEDIA_VISIBILITY
        )
        val keepChatsArchived = SharedPrefrencesUtil.getBooleanPrefrences(
            this,
            SharedPrefrencesConstant.KEEP_CHAT_ARCHIVED
        )

        enterKeySendSwitch.isChecked = enterSend!!
        mediaVisibilitySwitch.isChecked = mediaVisibility!!
        keepChatsArchiveSwitch.isChecked = keepChatsArchived!!
    }

    private fun keepChatsArchiveSwitchClickHandler() {
        if (keepChatsArchiveSwitch.isChecked) {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.KEEP_CHAT_ARCHIVED,
                true
            )
        } else {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.KEEP_CHAT_ARCHIVED,
                false
            )
        }
    }

    private fun mediaVisibilitySwitchClickHandler() {
        if (mediaVisibilitySwitch.isChecked) {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.MEDIA_VISIBILITY,
                true
            )
        } else {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.MEDIA_VISIBILITY,
                false
            )
        }
    }

    private fun enterKeySendSwitchClickHandler() {
        if (enterKeySendSwitch.isChecked) {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.ENTER_KEY_SEND,
                true
            )
        } else {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.ENTER_KEY_SEND,
                false
            )
        }
    }

    private fun appLanguageClickHandler() {
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.appThemeChooserDialogTheme)
        alertDialogBuilder.setTitle("App Language")

        val deviceLanguage = Locale.getDefault().displayLanguage
        val view = layoutInflater.inflate(R.layout.language_selector, null)
        view.findViewById<RadioButton>(R.id.deviceLanguage).text =
            getString(R.string.lan_device, deviceLanguage)
        val radioGroup = view.findViewById<RadioGroup>(R.id.appLanguageRadioGroup)
        radioGroup.check(getCurrentAppLanguageId())

        alertDialogBuilder.setView(view)
        val alertDialog = alertDialogBuilder.create()

        radioGroup.setOnCheckedChangeListener { group, _ ->
            group.forEach { rb ->
                val radioButton = rb as RadioButton
                if (radioGroup.checkedRadioButtonId == radioButton.id) {
                    chatSettingAppLanguage.text = radioButton.text
                    SharedPrefrencesUtil.setStringPrefrences(
                        this,
                        SharedPrefrencesConstant.CURRENT_APP_LANGUAGE,
                        radioButton.text.toString()
                    )
                    updateCurrentAppLanguageText()
                }
            }
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun getCurrentAppLanguageId(): Int {
        val currentAppLanguage = SharedPrefrencesUtil.getStringPrefrences(
            this,
            SharedPrefrencesConstant.CURRENT_APP_LANGUAGE
        )

        return when (currentAppLanguage) {
            getString(R.string.lan_english) -> R.id.englishLanguage
            getString(R.string.lan_hindi) -> R.id.hindiLanguage
            getString(R.string.lan_gujarati) -> R.id.gujaratiLanguage
            getString(R.string.lan_marathi) -> R.id.marathiLanguage
            getString(R.string.lan_punjabi) -> R.id.punjabiLanguage
            else -> R.id.deviceLanguage
        }
    }

    private fun fontSizeClickHandler() {
        val alertDialog = AlertDialog.Builder(this, R.style.appThemeChooserDialogTheme)
        val view = LayoutInflater.from(this).inflate(R.layout.font_size_selector, null)
        alertDialog.setView(view)

        val alertDialogBuilder = alertDialog.create()
        val radioGroup: RadioGroup = view.findViewById(R.id.chatFontSizeRadioGroup)
        radioGroup.check(getCurrentThemeId())

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            Log.d(
                ApplicationLoggingConstant.RADIO_BUTTON_CLICK_CHANGE.toString(),
                "Radio Group Changed"
            )
            updateChatFontSize(checkedId)
            updateCurrentChatFontSizeText()
            alertDialogBuilder.dismiss()
        }

        alertDialog.show()
    }

    private fun updateCurrentChatFontSizeText() {
        when (SharedPrefrencesUtil.getStringPrefrences(
            this,
            SharedPrefrencesConstant.FONT_SIZE
        )) {
            SharedPrefrencesConstant.SMALL_FONT_SIZE -> currentChatFontSize.text =
                ApplicationConstant.CHAT_FONT_SMALL
            SharedPrefrencesConstant.LARGE_FONT_SIZE -> currentChatFontSize.text =
                ApplicationConstant.CHAT_FONT_LARGE
            else -> currentChatFontSize.text =
                ApplicationConstant.CHAT_FONT_MEDIUM
        }
    }

    private fun updateChatFontSize(checkedId: Int) {
        val fontSize: String = when (checkedId) {
            R.id.chatFontSizeSmall -> SharedPrefrencesConstant.SMALL_FONT_SIZE
            R.id.chatFontSizeLarge -> SharedPrefrencesConstant.LARGE_FONT_SIZE
            else -> SharedPrefrencesConstant.MEDIUM_FONT_SIZE
        }

        SharedPrefrencesUtil.setStringPrefrences(
            this,
            SharedPrefrencesConstant.FONT_SIZE,
            fontSize
        )
    }

    private fun keepChatsArchiveClickHandler() {
        if (keepChatsArchiveSwitch.isChecked) {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.KEEP_CHAT_ARCHIVED,
                true
            )
            keepChatsArchiveSwitch.isChecked = true
        } else {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.KEEP_CHAT_ARCHIVED,
                false
            )
            keepChatsArchiveSwitch.isChecked = false
        }
    }

    private fun mediaVisibilityClickHandler() {
        if (mediaVisibilitySwitch.isChecked) {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.MEDIA_VISIBILITY,
                true
            )
            mediaVisibilitySwitch.isChecked = true
        } else {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.MEDIA_VISIBILITY,
                false
            )
            mediaVisibilitySwitch.isChecked = false
        }
    }

    private fun enterKeySendClickHandler() {
        if (enterKeySendSwitch.isChecked) {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.ENTER_KEY_SEND,
                true
            )
            enterKeySendSwitch.isChecked = true
        } else {
            SharedPrefrencesUtil.setBooleanPrefrences(
                this,
                SharedPrefrencesConstant.ENTER_KEY_SEND,
                false
            )
            enterKeySendSwitch.isChecked = false
        }
    }

    private fun themeClickHandler() {
        val alertDialog = AlertDialog.Builder(this, R.style.appThemeChooserDialogTheme)
        val view = LayoutInflater.from(this).inflate(R.layout.theme_selector_layout, null)
        alertDialog.setView(view)
        val radioGroup: RadioGroup = view.findViewById(R.id.appThemeRadioGroup)
        radioGroup.check(getCurrentThemeId())

        alertDialog
            .setTitle("Choose theme")
            .setPositiveButton("OK") { dialog, _ ->
                handleAppThemeChange(radioGroup.checkedRadioButtonId)
                dialog.dismiss()
            }
            .setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss() }

        val alertDialogBuilder = alertDialog.create()
        alertDialogBuilder.setOnShowListener {
            alertDialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#f15c6d"))
            alertDialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#f15c6d"))
        }
        alertDialog.show()
    }

    private fun handleAppThemeChange(checkedRadioButtonId: Int) {
        when (checkedRadioButtonId) {
            R.id.appThemeLight -> {
                SharedPrefrencesUtil.setStringPrefrences(
                    this,
                    SharedPrefrencesConstant.APP_THEME,
                    SharedPrefrencesConstant.THEME_LIGHT
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            R.id.appThemeDark -> {
                SharedPrefrencesUtil.setStringPrefrences(
                    this,
                    SharedPrefrencesConstant.APP_THEME,
                    SharedPrefrencesConstant.THEME_DARK
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            R.id.appThemeSystemDefault -> {
                SharedPrefrencesUtil.setStringPrefrences(
                    this,
                    SharedPrefrencesConstant.APP_THEME,
                    SharedPrefrencesConstant.THEME_SYSTEM
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }

        updateCurrentThemeText()
    }

    private fun updateCurrentThemeText() {
        when (SharedPrefrencesUtil.getStringPrefrences(this, SharedPrefrencesConstant.APP_THEME)) {
            SharedPrefrencesConstant.THEME_DARK -> currentTheme.text =
                ApplicationConstant.THEME_DARK

            SharedPrefrencesConstant.THEME_LIGHT -> currentTheme.text =
                ApplicationConstant.THEME_LIGHT

            SharedPrefrencesConstant.THEME_SYSTEM -> currentTheme.text =
                ApplicationConstant.THEME_SYSTEM

            else -> currentTheme.text =
                ApplicationConstant.THEME_SYSTEM
        }
    }


    private fun getCurrentThemeId(): Int {
        return when (SharedPrefrencesUtil.getStringPrefrences(
            this,
            SharedPrefrencesConstant.APP_THEME
        )) {
            SharedPrefrencesConstant.THEME_DARK -> R.id.appThemeDark
            SharedPrefrencesConstant.THEME_LIGHT -> R.id.appThemeLight
            else -> R.id.appThemeSystemDefault
        }
    }
}