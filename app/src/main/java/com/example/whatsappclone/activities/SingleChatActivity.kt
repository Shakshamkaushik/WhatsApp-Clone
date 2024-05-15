package com.example.whatsappclone.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.adapter.SingleChatAdapter
import com.example.whatsappclone.constants.ApplicationLoggingConstant
import com.example.whatsappclone.constants.ChatConstants
import com.example.whatsappclone.constants.FireBaseConstant
import com.example.whatsappclone.constants.SharedPrefrencesConstant
import com.example.whatsappclone.models.firebase.ChatMessage
import com.example.whatsappclone.utils.SecurityUtils
import com.example.whatsappclone.utils.SharedPrefrencesUtil
import com.example.whatsappclone.utils.TimeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject

class SingleChatActivity : AppCompatActivity(), TextWatcher {

    private lateinit var singleChatToolbar: Toolbar
    private lateinit var backToAllChat: LinearLayout
    private lateinit var chatNameOnlineStatusLL: LinearLayout
    private lateinit var chatMessageAttachmentIcon: ImageView
    private lateinit var chatMessageCurrencyIcon: ImageView
    private lateinit var ivBackArrow: ImageView
    private lateinit var chatMessageCameraIcon: ImageView
    private lateinit var chatUserProfile: ImageView
    private lateinit var chatContactName: TextView
    private lateinit var chatContactLastSeen: TextView
    private lateinit var chatMessage: EditText
    private lateinit var sendMessageButton: FloatingActionButton
    private lateinit var recordVoiceButton: FloatingActionButton


    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatRecyclerViewAdapter: SingleChatAdapter
    private var receiverUid: String? = null
    private var senderUid: String? = null

    private lateinit var messageCollectionRef: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_chat)
        init()
    }

    private fun init() {
        messageCollectionRef = FirebaseFirestore.getInstance()
            .collection(FireBaseConstant.FIREBASE_COLLECTIONS_OF_MESSAGES)
        senderUid = FirebaseAuth.getInstance().currentUser?.uid
        receiverUid = intent.getStringExtra(ChatConstants.CHAT_PROFILE_USER_UID)

        singleChatToolbar = findViewById(R.id.singleChatToolBar)
        backToAllChat = findViewById(R.id.backToAllChatLL)
        ivBackArrow = findViewById(R.id.ivBAckArrowSingleChat)
        chatNameOnlineStatusLL = findViewById(R.id.chatNameOnlineStatusLL)
        chatMessageAttachmentIcon = findViewById(R.id.ivAttachIcon)
        chatMessageCurrencyIcon = findViewById(R.id.ivRupeeIcon)
        chatMessageCameraIcon = findViewById(R.id.ivCameraIcon)
        chatUserProfile = findViewById(R.id.ivUserProfilePicture)
        chatContactName = findViewById(R.id.tvChatContactName)
        chatContactLastSeen = findViewById(R.id.tvChatOnlineStatus)
        chatMessage = findViewById(R.id.etMessageText)
        sendMessageButton = findViewById(R.id.messageFloatingBtn)
        recordVoiceButton = findViewById(R.id.voiceFloatingBtn)
        chatRecyclerView = findViewById(R.id.chatSingleScreenRecyclerView)

        chatMessage.addTextChangedListener(this)

        sendMessageButton.setOnClickListener { sendMessageButtonClickHandler() }
        chatNameOnlineStatusLL.setOnClickListener { showProfile() }
        backToAllChat.setOnClickListener {
            startActivity(
                Intent(
                    this@SingleChatActivity,
                    HomeMainScreenActivity::class.java
                )
            )
            finish()
        }
        ivBackArrow.setOnClickListener {
            startActivity(Intent(this, HomeMainScreenActivity::class.java))
            finish()
        }


    }

    private fun showProfile() {
        val profileIntent = Intent(this@SingleChatActivity, ChatProfileInfoActivity::class.java)
        val extras = intent.extras
        if (extras != null) {
            profileIntent.putExtras(extras)
        }
        startActivity(profileIntent)
    }

    private fun sendMessageButtonClickHandler() {
        if (chatMessage.text.isNotEmpty()) {
            val msg = chatMessage.text.toString()
            chatMessage.setText("")
            sendMessage(msg)
        }
        fetAndUpdateChatDataForCurrentChat()
    }

    private fun sendMessage(msg: String) {
        val msgObject = ChatMessage(
            message = SecurityUtils.encryptMessage(msg, senderUid, receiverUid),
            chatMessageTime = TimeUtils.getMessageDateTime(),
            time = System.currentTimeMillis(),
            senderUid = senderUid,
            isMessageDelivered = false,
            receiverUid = receiverUid,
            isMessageSeen = false
        )

        uploadMessage(msgObject)
    }

    private fun uploadMessage(msgObject: ChatMessage): String? {
        messageCollectionRef.add(msgObject).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d(
                    ApplicationLoggingConstant.FIREBASE_CHAT_MESSAGE_UPLOAD.toString(),
                    "Message stored in the database"
                )
            } else {
                Log.d(
                    ApplicationLoggingConstant.FIREBASE_CHAT_MESSAGE_UPLOAD.toString(),
                    it.exception?.message.toString()
                )
            }
        }.addOnFailureListener {
            Log.d(
                ApplicationLoggingConstant.FIREBASE_CHAT_MESSAGE_UPLOAD.toString(),
                it.message.toString()
            )
        }

        return null
    }

    override fun onResume() {
        super.onResume()
        val bundle = intent.extras
        chatContactName.text = bundle?.getString(ChatConstants.CHAT_PROFILE_NAME).toString()
        chatRecyclerViewAdapter = SingleChatAdapter(this)
        chatRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        chatRecyclerView.adapter = chatRecyclerViewAdapter
        val profilePicture = bundle?.getString(ChatConstants.CHAT_PROFILE_PICTURE)
        if (profilePicture != null && profilePicture != "null") {
            chatUserProfile.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            chatUserProfile.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            chatUserProfile.scaleType = ImageView.ScaleType.FIT_XY
            chatUserProfile.imageTintMode = null
            Glide.with(this).load(profilePicture).into(chatUserProfile)
        }

        fetAndUpdateChatDataForCurrentChat()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetAndUpdateChatDataForCurrentChat() {
        val sender = senderUid
        val receiver = receiverUid

        messageCollectionRef.orderBy("time", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                val docs = it.documents
                val chats = arrayListOf<ChatMessage>()
                val chatIds = arrayListOf<String>()

                docs.forEach { documentSnapShot ->
                    val message = documentSnapShot.toObject<ChatMessage>()

                    if (message != null) {
                        if ((message.getSenderUid() == receiver && message.getReceiverUid() == sender) ||
                            (message.getSenderUid() == sender && message.getReceiverUid() == receiver)
                        ) {
                            chats.add(message)
                            chatIds.add(documentSnapShot.id)
                        }
                    }
                }
                chatRecyclerViewAdapter.updateChatMessageDataset(chats)
                chatRecyclerViewAdapter.notifyDataSetChanged()
                updateMessageReadStatus(chats, chatIds)
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateMessageReadStatus(
        chats: ArrayList<ChatMessage>,
        chatIds: ArrayList<String>
    ) {
        val isReadReceiptEnabled = SharedPrefrencesUtil.getBooleanPrefrences(
            this,
            SharedPrefrencesConstant.REGISTERED_USER_IS_RED_RECEIPT_ENABLED
        )
        val currentUSerId = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUSerId != null && isReadReceiptEnabled!!) {
            chats.forEachIndexed { index, chatMessage ->
                if (chatMessage.getReceiverUid() == currentUSerId && !chatMessage.getIsMessageSeen()) {
                    val messageId = chatIds[index]
                    messageCollectionRef.document(messageId).update("isMessageSeen", true)
                }

            }
            chatRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (chatMessage.text.isEmpty()) {
            sendMessageButton.visibility = View.VISIBLE
            recordVoiceButton.visibility = View.GONE
            chatMessageCameraIcon.visibility = View.GONE
            chatMessageCurrencyIcon.visibility = View.GONE
            changeMarginEndOfAttachMent(4F, 8F)
        }

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isEmpty()) {
            recordVoiceButton.visibility = View.VISIBLE
            sendMessageButton.visibility = View.GONE
            chatMessageCameraIcon.visibility = View.VISIBLE
            chatMessageCurrencyIcon.visibility = View.VISIBLE
            changeMarginEndOfAttachMent(0F, 16F)
        }
    }

    private fun changeMarginEndOfAttachMent(marginStart: Float, marginEnd: Float) {
        val start = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, marginStart, resources.displayMetrics
        )

        val end = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, marginEnd, resources.displayMetrics
        )

        val layoutParams = chatMessageAttachmentIcon.layoutParams as LinearLayout.LayoutParams
        layoutParams.marginEnd = end.toInt()
        layoutParams.marginStart = start.toInt()
        chatMessageAttachmentIcon.layoutParams = layoutParams
    }
}