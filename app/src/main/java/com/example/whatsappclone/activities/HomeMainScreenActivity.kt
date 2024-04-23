package com.example.whatsappclone.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.example.whatsappclone.R
import com.example.whatsappclone.adapter.viewPageAdapter
import com.example.whatsappclone.constants.SharedPrefrencesConstant
import com.example.whatsappclone.utils.SharedPrefrencesUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeMainScreenActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: viewPageAdapter
    private lateinit var fabBtn: FloatingActionButton
    private lateinit var fabStatusBtn: FloatingActionButton
    private lateinit var fabCallBtn: FloatingActionButton

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.chatBottomSheet -> {
                    viewPager2.currentItem = 0

                    fabBtn.visibility = View.VISIBLE
                    fabCallBtn.visibility = View.GONE
                    fabStatusBtn.visibility = View.GONE
                    return@OnNavigationItemSelectedListener true
                }

                R.id.whatsappUpdatesBottomSheet -> {
                    viewPager2.currentItem = 1

                    fabBtn.visibility = View.GONE
                    fabCallBtn.visibility = View.GONE
                    fabStatusBtn.visibility = View.VISIBLE
                    return@OnNavigationItemSelectedListener true
                }

                R.id.callsBottomSheet -> {
                    viewPager2.currentItem = 2

                    fabBtn.visibility = View.GONE
                    fabCallBtn.visibility = View.VISIBLE
                    fabStatusBtn.visibility = View.GONE
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_main_screen)
        bottomNavigationView = findViewById(R.id.mainScreenBottomNavigationView)
        viewPager2 = findViewById(R.id.viewPager)
        fabBtn = findViewById(R.id.floatingActionButton)
        fabStatusBtn = findViewById(R.id.floatingActionStatusButton)
        fabCallBtn = findViewById(R.id.floatingActionCallButton)
        adapter = viewPageAdapter(supportFragmentManager, lifecycle)


        viewPager2.adapter = adapter
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        fabBtn.setOnClickListener { chatFabClickHandler() }
        fabStatusBtn.setOnClickListener { Toast.makeText(this,"Not Available!",Toast.LENGTH_SHORT).show() }
        fabCallBtn.setOnClickListener { Toast.makeText(this,"Not Available!",Toast.LENGTH_SHORT).show() }
        init()
        setAppTheme()
    }



    //init not working
    private fun init() {
        viewPager2 = findViewById(R.id.viewPager)
        fabBtn = findViewById(R.id.floatingActionButton)
//        val badge = bottomNavigationView.getOrCreateBadge(R.id.chatBottomSheet);
//        badge.isVisible = true;
//        badge.number = 99;
        adapter = viewPageAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter



        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        bottomNavigationView.selectedItemId = R.id.chatBottomSheet

                        fabBtn.visibility = View.VISIBLE
                        fabCallBtn.visibility = View.GONE
                        fabStatusBtn.visibility = View.GONE
                    }

                    1 -> {
                        bottomNavigationView.selectedItemId = R.id.whatsappUpdatesBottomSheet

                        fabBtn.visibility = View.GONE
                        fabCallBtn.visibility = View.GONE
                        fabStatusBtn.visibility = View.VISIBLE

                    }

                    else -> {
                        bottomNavigationView.selectedItemId = R.id.callsBottomSheet

                        fabBtn.visibility = View.GONE
                        fabCallBtn.visibility = View.VISIBLE
                        fabStatusBtn.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun setAppTheme() {
        val appTheme =
            SharedPrefrencesUtil.getStringPrefrences(this, SharedPrefrencesConstant.APP_THEME)

        if (appTheme != null) {
            when (appTheme) {
                SharedPrefrencesConstant.THEME_DARK -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                SharedPrefrencesConstant.THEME_LIGHT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                SharedPrefrencesConstant.THEME_SYSTEM -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }

    private fun chatFabClickHandler() {
        val allContactActivityIntent = Intent(this, AllContactActivity::class.java)
        startActivity(allContactActivityIntent)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(a)

    }

}

