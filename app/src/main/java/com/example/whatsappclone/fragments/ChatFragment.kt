
package com.example.whatsappclone.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.activities.PaymentActivity
import com.example.whatsappclone.activities.SettingActivity
import com.example.whatsappclone.activities.SingleChatActivity
import com.example.whatsappclone.adapter.ChatFragmentAdapter
import com.example.whatsappclone.constants.ChatConstants
import com.example.whatsappclone.constants.FireBaseConstant
import com.example.whatsappclone.interfaces.IChatClick
import com.example.whatsappclone.models.firebase.ChatMessage
import com.example.whatsappclone.models.firebase.User
import com.example.whatsappclone.utils.GlobalLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject


class ChatFragment : Fragment(), IChatClick, EventListener<QuerySnapshot> {
    private lateinit var ivMenuOption: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatFragmentAdapter

    private lateinit var messageCollectionRef: CollectionReference
    private lateinit var userCollectionRef: CollectionReference
    private var currentUser: FirebaseUser? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        GlobalLog.showLog("TAG", "IN CHAT FRAGMENT")
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        ivMenuOption = view.findViewById(R.id.iv_chat_menu_option)
        recyclerView = view.findViewById(R.id.chatFragmentRecyclerView)

        currentUser = FirebaseAuth.getInstance().currentUser
        messageCollectionRef = FirebaseFirestore.getInstance()
            .collection(FireBaseConstant.FIREBASE_COLLECTIONS_OF_MESSAGES)
        userCollectionRef =
            FirebaseFirestore.getInstance().collection(FireBaseConstant.COLLECTIONS_OF_USERS)
        ivMenuOption.setOnClickListener {
            showPopUpMenu()
        }

        messageCollectionRef.addSnapshotListener(this)

        return view
    }

    override fun onResume() {
        super.onResume()
        GlobalLog.showLog("TAG", "IN CHAT FRAGMENT onResume")
        adapter = ChatFragmentAdapter(requireContext(), this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        setMessagesStateToDelivered()
        getExistingChatUsersForCurrentUser()
    }

    private fun setMessagesStateToDelivered() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            messageCollectionRef.whereEqualTo("receiverUid", currentUserUid).get()
                .addOnSuccessListener {
                    val docs = it.documents

                    docs.forEach { documentSnapshot ->
                        val msg = documentSnapshot.toObject<ChatMessage>()
                        if (msg != null) {
                            if (!msg.getIsMessageDelivered()) {
                                messageCollectionRef.document(documentSnapshot.id)
                                    .update("isMessageDelivered", true)
                            }
                        }

                    }
                }
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getExistingChatUsersForCurrentUser() {
        val chatUsers: ArrayList<User> = arrayListOf()
        userCollectionRef.get().addOnSuccessListener { querySnapShot ->
            val allUsers = querySnapShot.documents
            allUsers.forEach { document ->
                val user = document.toObject<User>()
                if (user != null) {
                    if (user.getUid() != currentUser?.uid) {
                        chatUsers.add(user)
                    }

                    chatUsers.sortBy {
                        it.getName()
                    }
                }
            }
            adapter.updateChatData(chatUsers)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showPopUpMenu() {
        val wrapper = ContextThemeWrapper(this.requireContext(), R.style.PopupMenu)
        val pm = PopupMenu(wrapper, ivMenuOption, Gravity.NO_GRAVITY)
        pm.menuInflater.inflate(R.menu.main_menu, pm.menu)
        pm.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.linked_devices_menu_item -> {
                    Toast.makeText(
                        this.requireContext(),
                        "Not Available Right Now",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                R.id.payments_menu_item -> {
                    val paymentIntent =
                        Intent(this@ChatFragment.requireContext(), PaymentActivity::class.java)
                    startActivity(paymentIntent)
                }

                R.id.setting_menu_item -> {
                    val settingIntent =
                        Intent(this@ChatFragment.requireContext(), SettingActivity::class.java)
                    startActivity(settingIntent)
                }

                else -> {
                    Toast.makeText(
                        this.requireContext(),
                        "Not Available Right Now",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            true
        })
        pm.show()
    }

    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            return
        }
        if (value != null && value.metadata.hasPendingWrites()) {
            getExistingChatUsersForCurrentUser()
        }
    }

    override fun onItemClicked(user: User?) {
        if (user != null) {
            val intent = Intent(this@ChatFragment.requireContext(), SingleChatActivity::class.java)
            val bundle = Bundle()

            bundle.putString(ChatConstants.CHAT_PROFILE_NAME, user.getName())
            bundle.putString(ChatConstants.CHAT_PROFILE_PICTURE, user.getProfilePictureUrl())
            bundle.putString(ChatConstants.CHAT_PROFILE_USER_UID, user.getUid())
            bundle.putString(ChatConstants.CHAT_PROFILE_LAST_SEEN, user.getLastSeen())
            bundle.putString(ChatConstants.CHAT_PROFILE_COUNTRY_CODE, user.getCountryCode())
            bundle.putString(ChatConstants.CHAT_PROFILE_PHONE_NUMBER, user.getMobileNumber())
            bundle.putString(ChatConstants.CHAT_PROFILE_STATUS, user.getProfileStatus())
            bundle.putString(ChatConstants.CHAT_PROFILE_STATUS_UPDATE_ON, user.getStatusUpdateOn())
            bundle.putBoolean(
                ChatConstants.CHAT_PROFILE_LAST_SEEN_VISIBLE,
                user.getIsLastSeenVisisble()
            )
            GlobalLog.showLog("Name", user.getName().toString())
            GlobalLog.showLog("profile_picture", user.getProfilePictureUrl().toString())
            GlobalLog.showLog("UID", user.getUid().toString())
            GlobalLog.showLog("LAST SEEN", user.getLastSeen().toString())
            GlobalLog.showLog("COUNTRY CODE", user.getCountryCode().toString())
            GlobalLog.showLog("MOBILE NUMBER", user.getMobileNumber().toString())
            GlobalLog.showLog("STATUS", user.getProfileStatus().toString())
            GlobalLog.showLog("STATUS UPDATE ON", user.getStatusUpdateOn().toString())
            GlobalLog.showLog("LAST SEEN VISIBLE", user.getIsLastSeenVisisble().toString())

            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
}

