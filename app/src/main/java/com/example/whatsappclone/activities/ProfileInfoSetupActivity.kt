package com.example.whatsappclone.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.whatsappclone.R
import com.example.whatsappclone.constants.ApplicationConstant
import com.example.whatsappclone.constants.FireBaseConstant
import com.example.whatsappclone.constants.SharedPrefrencesConstant
import com.example.whatsappclone.models.firebase.User
import com.example.whatsappclone.utils.SharedPrefrencesUtil
import com.example.whatsappclone.utils.TimeUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@RequiresApi(Build.VERSION_CODES.O)
class ProfileInfoSetupActivity : AppCompatActivity() {
    private lateinit var setProfilePicture: CardView
    private lateinit var selectProfilePicture: ImageView
    private lateinit var profileName: EditText
    private lateinit var btnProfileSetup: Button
    private lateinit var imageLaunch: ActivityResultLauncher<Intent>
    private lateinit var profileProgressBar: ProgressBar


    private var currentUser: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_info_setup)
        var profilePictureUri: Uri? = null
        init()
        selectProfilePicture.setOnClickListener {
            selectProfilePictureClickHandler()
        }
        btnProfileSetup.setOnClickListener {
            setUpProfile(profilePictureUri)
        }

        imageLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && currentUser != null) {
                selectProfilePicture.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                selectProfilePicture.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                selectProfilePicture.scaleType = ImageView.ScaleType.FIT_XY
                selectProfilePicture.imageTintMode = null
                profilePictureUri = it.data?.data
                Glide.with(this).load(profilePictureUri).into(selectProfilePicture)
                profileProgressBar.visibility = View.GONE
            }
        }
    }

    fun init() {
        setProfilePicture = findViewById(R.id.profilePictureCardView)
        selectProfilePicture = findViewById(R.id.ivSelectImageForProfilepicture)
        profileName = findViewById(R.id.etProfileName)
        btnProfileSetup = findViewById(R.id.btnSetupProfile)
        profileProgressBar = findViewById(R.id.profilePictureProgressBar)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
        storageRef = FirebaseStorage.getInstance().reference


    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        if (currentUser != null && currentUser!!.displayName != null) {
            profileName.text = Editable.Factory.getInstance().newEditable(currentUser!!.displayName)
        }
        if (currentUser != null && currentUser!!.photoUrl != null) {
            profileProgressBar.visibility = View.VISIBLE
            selectProfilePicture.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            selectProfilePicture.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            selectProfilePicture.imageTintMode = null
            selectProfilePicture.scaleType = ImageView.ScaleType.CENTER_CROP

            val layoutParams = FrameLayout.LayoutParams(setProfilePicture.layoutParams)
            layoutParams.setMargins(0)

            Glide.with(this).load(currentUser?.photoUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        profileProgressBar.visibility = View.INVISIBLE
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        profileProgressBar.visibility = View.INVISIBLE
                        return false
                    }

                }).into(selectProfilePicture)
            profileProgressBar.visibility = View.GONE
        }
    }


    private fun selectProfilePictureClickHandler() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val pickImageIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageIntent.type = "image/*"
        val choserIntent = Intent.createChooser(intent, "Select Image For Profile Picture")
        choserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickImageIntent))
        imageLaunch.launch(choserIntent)
    }

    private fun setUpProfile(profilePictureUri: Uri?) {
        profileProgressBar.visibility = View.VISIBLE
        btnProfileSetup.isEnabled = false
        selectProfilePicture.isEnabled = false
        val name = profileName.text.toString()

        when {
            name.trim().isEmpty() -> {
                Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show()
            }

            currentUser == null -> {
                Toast.makeText(this, "Current User is NULL $currentUser", Toast.LENGTH_SHORT).show()
            }

            else -> {
                updateProfileInfo(name, profilePictureUri)
            }
        }
        profileProgressBar.visibility = View.GONE
    }

    private fun updateProfileInfo(name: String, profilePictureUri: Uri?) {
        profileProgressBar.visibility = View.VISIBLE
        btnProfileSetup.isEnabled = false
        selectProfilePicture.isEnabled = false


        val updateProfileBuilder = UserProfileChangeRequest.Builder().setDisplayName(name)

        if (currentUser != null) {
            if (profilePictureUri != null) {
                val image =
                    storageRef.child("${FireBaseConstant.DIRECTORY_PATH_OF_PROFILE_PICTURE}/${currentUser?.uid}")
                image.putFile(profilePictureUri)
                    .addOnSuccessListener {
                        val uploadSessionUri = it.uploadSessionUri.toString()
                        val str1 =
                            uploadSessionUri.substring(0, uploadSessionUri.indexOf("&uploadType"))
                        val finalUrl = "$str1&alt=media"
                        updateProfileBuilder.photoUri = Uri.parse(finalUrl)
                        updateFirebaseProfile(currentUser!!, updateProfileBuilder, name)
                    }
            } else {
                updateFirebaseProfile(currentUser!!, updateProfileBuilder, name)
                profileProgressBar.visibility = View.GONE
            }

        } else {
            Toast.makeText(this, "Enter the Details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFirebaseProfile(
        currentUser: FirebaseUser,
        updateProfileBuilder: UserProfileChangeRequest.Builder,
        name: String
    ) {
        currentUser.updateProfile(updateProfileBuilder.build()).addOnCompleteListener {
            if (it.isSuccessful) {
                SharedPrefrencesUtil.setStringPrefrences(
                    this,
                    SharedPrefrencesConstant.REGISTERED_USER_NAME,
                    name
                )
                saveOrUpdateDataUserCollection(currentUser)
            }
        }
    }


    private fun saveOrUpdateDataUserCollection(currentUser: FirebaseUser?) {

        val userCollection =
            FirebaseFirestore.getInstance().collection(FireBaseConstant.COLLECTIONS_OF_USERS)


        if (currentUser != null) {

            userCollection.document(currentUser.uid).get()
                .addOnSuccessListener {
                    if (it.data != null) {
                        val user = it.toObject<User>()
                        if (user != null) {
                            SharedPrefrencesUtil.setStringPrefrences(
                                this,
                                SharedPrefrencesConstant.REGISTERED_USER_UID,
                                currentUser.uid
                            )

                            SharedPrefrencesUtil.setBooleanPrefrences(
                                this,
                                SharedPrefrencesConstant.REGISTERED_USER_IS_RED_RECEIPT_ENABLED,
                                user.getIsReadReceiptOn()
                            )

                            SharedPrefrencesUtil.setStringPrefrences(
                                this,
                                SharedPrefrencesConstant.USER_PROFILE_STATUS,
                                user.getProfileStatus()
                            )

                            updateCurrentUserInfo(
                                currentUser.uid,
                                currentUser.displayName,
                                currentUser.photoUrl,
                                user
                            )
                        }
                    } else {
                        SharedPrefrencesUtil.setStringPrefrences(
                            this,
                            SharedPrefrencesConstant.USER_PROFILE_STATUS,
                            ApplicationConstant.DEFAULT_USER_PROFILE_STATUS
                        )
                        addCurrentUserToCollection(
                            currentUser.uid,
                            currentUser.displayName,
                            currentUser.photoUrl
                        )

                    }
                }
                .addOnFailureListener {
                    println("Error searching user.")
                }
        }
    }


    private fun addCurrentUserToCollection(uid: String, displayName: String?, photoUrl: Uri?) {
        val userCollection =
            FirebaseFirestore.getInstance().collection(FireBaseConstant.COLLECTIONS_OF_USERS)
        val user = User(
            name = displayName,
            uid = uid,
            profilePictureUrl = photoUrl.toString(),
            countryCode = SharedPrefrencesUtil.getStringPrefrences(
                this,
                SharedPrefrencesConstant.REGISTERED_COUNTRY_CODE
            ),
            mobileNumber = SharedPrefrencesUtil.getStringPrefrences(
                this,
                SharedPrefrencesConstant.REGISTERED_MOBILE_NUMBER
            ),
            isLastSeenVisible = true,
            lastSeen = "",
            profileStatus = ApplicationConstant.DEFAULT_USER_PROFILE_STATUS,
            isOnline = false,
            statusUpdatedOn = TimeUtils.getCurrentDateAndTime(),
            profileCreatedOn = TimeUtils.getCurrentDateAndTime(),
            isReadReceiptEnabled = true
        )
        SharedPrefrencesUtil.setStringPrefrences(
            this,
            SharedPrefrencesConstant.REGISTERED_USER_UID,
            user.getUid()!!
        )

        SharedPrefrencesUtil.setBooleanPrefrences(
            this,
            SharedPrefrencesConstant.REGISTERED_USER_IS_RED_RECEIPT_ENABLED,
            true
        )

        userCollection.document(user.getUid()!!).set(user).addOnCompleteListener {
            launchMainActivity()
        }.addOnFailureListener {
            launchMainActivity()
        }
    }

    private fun updateCurrentUserInfo(
        userUid: String,
        displayName: String?,
        profilrPictureUrl: Uri?,
        user: User
    ) {
        val userCollection =
            FirebaseFirestore.getInstance().collection(FireBaseConstant.COLLECTIONS_OF_USERS)

        val existinguser = user
        val dataUpdates = hashMapOf<String, Any>()
        var isProfileUpdated = false

        if (displayName != user.getName()) {
            existinguser.setName(displayName)
            dataUpdates["name"] = displayName.toString()
            isProfileUpdated = true
        }

        if (profilrPictureUrl.toString() != user.getProfilePictureUrl()) {
            existinguser.setProfilePictureUrl(profilrPictureUrl.toString())
            isProfileUpdated = true
        }

        if (isProfileUpdated) {
            userCollection.document(userUid).update(dataUpdates).addOnCompleteListener {
                launchMainActivity()
            }.addOnFailureListener {
                launchMainActivity()
            }
        } else {
            launchMainActivity()
        }

    }


    private fun launchMainActivity() {
        SharedPrefrencesUtil.setBooleanPrefrences(
            this,
            SharedPrefrencesConstant.PROFILE_INFO_SETUP_COMPLETED,
            true
        )
        profileProgressBar.visibility = View.INVISIBLE
        btnProfileSetup.isEnabled = true
        selectProfilePicture.isEnabled = true
        val mainActivityIntent = Intent(this, HomeMainScreenActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

}