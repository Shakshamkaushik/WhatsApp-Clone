@file:Suppress("SameParameterValue")

package com.example.whatsappclone.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.adapter.AllContactAdapter
import com.example.whatsappclone.constants.ChatConstants
import com.example.whatsappclone.constants.FireBaseConstant
import com.example.whatsappclone.constants.SharedPrefrencesConstant
import com.example.whatsappclone.interfaces.IContactClick
import com.example.whatsappclone.models.firebase.User
import com.example.whatsappclone.utils.SharedPrefrencesUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class AllContactActivity : AppCompatActivity(), IContactClick {
    private lateinit var toolbar: Toolbar
    private lateinit var allContactAdapter: AllContactAdapter
    private lateinit var allContactRecyclerView: RecyclerView
    private lateinit var contactCount: TextView
    private lateinit var BackArrow: ImageView
    private lateinit var refreshProgressBar: MenuItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_contact)

        toolbar = findViewById(R.id.toolbar)
        BackArrow = findViewById(R.id.ivBackArrow)
        contactCount = findViewById(R.id.tvContactCount)
        allContactRecyclerView = findViewById(R.id.allContactRecyclerView)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)

        BackArrow.setOnClickListener {
            startActivity(Intent(this, HomeMainScreenActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        allContactAdapter = AllContactAdapter(this, this)
        allContactRecyclerView.layoutManager = LinearLayoutManager(this)
        allContactRecyclerView.adapter = allContactAdapter
        updateTotalCount()
        refreshContactList(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.all_contacts_menu, menu)

        val menuItem = menu?.findItem(R.id.refreshContactProgressBarMenuItem)
        if (menuItem != null) {
            refreshProgressBar = menuItem
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.refreshContact) {
            refreshContactList(true)
        }
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshContactList(isManuallyRefresh: Boolean) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserId != null) {
            val userCollection =
                FirebaseFirestore.getInstance().collection(FireBaseConstant.COLLECTIONS_OF_USERS)

            if (isManuallyRefresh) {
                refreshProgressBar.isVisible = true
            }
            userCollection.whereNotEqualTo(FieldPath.documentId(), currentUserId).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result.documents
                        SharedPrefrencesUtil.setIntegerPrefrences(
                            this,
                            SharedPrefrencesConstant.CONTACTS_COUNT,
                            result.size
                        )


                        allContactAdapter.updateContactDataSet(result)
                        allContactAdapter.notifyDataSetChanged()

                        if (isManuallyRefresh) {
                            refreshProgressBar.isVisible = false
                        }
                        updateTotalCount()
                    }


                }.addOnFailureListener {

                }
        }
    }

    private fun updateTotalCount() {
        contactCount.text = getString(
            R.string.all_contacts_count,
            SharedPrefrencesUtil.getIntegerPrefrences(this, SharedPrefrencesConstant.CONTACTS_COUNT)
        )
    }

    override fun onItemClicked(user: User?) {
        if (user != null) {
            val singleChatIntent = Intent(this@AllContactActivity, SingleChatActivity::class.java)
            val bundle = Bundle()
            Log.d(
                "SECURITY LOG",
                "Contact item click: ${user.getUid()}"
            )
            Log.d(
                "SECURITY LOG",
                "Contact item click: ${user.getUid()}"
            )
//            GlobalLog.showLog(
//                "TAG",
//                "CHAT_PROFILE_PICTURE  ${user.getProfilePictureUrl().toString()}"
//            )
//            GlobalLog.showLog("TAG", "CHAT_PROFILE_USER_UID  ${user.getUid().toString()}")
//            GlobalLog.showLog("TAG", "CHAT_PROFILE_LAST_SEEN  ${user.getLastSeen()}")
//            GlobalLog.showLog(
//                "TAG",
//                "CHAT_PROFILE_COUNTRY_CODE  ${user.getCountryCode().toString()}"
//            )
//            GlobalLog.showLog(
//                "TAG",
//                "CHAT_PROFILE_PHONE_NUMBER  ${user.getMobileNumber().toString()}"
//            )
//            GlobalLog.showLog("TAG", "CHAT_PROFILE_PHONE_NUMBER  ${user.getProfileStatus()}")
//            GlobalLog.showLog(
//                "TAG",
//                "CHAT_PROFILE_STATUS_UPDATE_ON  ${user.getStatusUpdateOn().toString()}"
//            )


            bundle.putString(ChatConstants.CHAT_PROFILE_NAME, user.getName().toString())
            bundle.putString(
                ChatConstants.CHAT_PROFILE_PICTURE,
                user.getProfilePictureUrl().toString()
            )
            bundle.putString(ChatConstants.CHAT_PROFILE_USER_UID, user.getUid().toString())
            bundle.putString(ChatConstants.CHAT_PROFILE_LAST_SEEN, user.getLastSeen())
            bundle.putString(
                ChatConstants.CHAT_PROFILE_COUNTRY_CODE,
                user.getCountryCode().toString()
            )
            bundle.putString(
                ChatConstants.CHAT_PROFILE_PHONE_NUMBER,
                user.getMobileNumber().toString()
            )
            bundle.putString(ChatConstants.CHAT_PROFILE_PHONE_NUMBER, user.getProfileStatus())
            bundle.putString(
                ChatConstants.CHAT_PROFILE_STATUS_UPDATE_ON,
                user.getStatusUpdateOn().toString()
            )
            bundle.putBoolean(
                ChatConstants.CHAT_PROFILE_LAST_SEEN_VISIBLE,
                user.getIsLastSeenVisisble()
            )
            singleChatIntent.putExtras(bundle)
            startActivity(singleChatIntent)
        }
    }
}