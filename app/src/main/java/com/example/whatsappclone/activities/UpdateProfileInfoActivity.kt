package com.example.whatsappclone.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.whatsappclone.R
import com.example.whatsappclone.constants.ApplicationConstant
import com.example.whatsappclone.constants.ApplicationLoggingConstant
import com.example.whatsappclone.constants.FireBaseConstant
import com.example.whatsappclone.constants.SharedPrefrencesConstant
import com.example.whatsappclone.models.firebase.User
import com.example.whatsappclone.utils.SharedPrefrencesUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import java.net.UnknownHostException

class UpdateProfileInfoActivity : AppCompatActivity() {

    private lateinit var updateToolbar: Toolbar
    private lateinit var updateProfileName: TextView
    private lateinit var updateProfilePhone: TextView
    private lateinit var updateProfileAbout: TextView
    private lateinit var updateImageView: ImageView
    private lateinit var updateNameRelativeLayout: RelativeLayout
    private lateinit var updateProfilePictureProgressBar: RelativeLayout
    private lateinit var updateProfilePictureButton: FloatingActionButton
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var imageChooserAction: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile_info)
        updateToolbar = findViewById(R.id.updateProfileToolbar)

        setSupportActionBar(updateToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color, theme
                )
            )
        )
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!


        updateProfileName = findViewById(R.id.tvUpdateProfileName)
        updateProfilePhone = findViewById(R.id.tvUpdateProfilePhone)
        updateProfileAbout = findViewById(R.id.tvUpdateProfileAbout)
        updateImageView = findViewById(R.id.ivProfileUpdate)
        updateNameRelativeLayout = findViewById(R.id.updateProfileUpdateRL)
        updateProfilePictureProgressBar = findViewById(R.id.updateProfilePictureProgressBar)
        updateProfilePictureButton = findViewById(R.id.newProfilePictureSettingFab)

        updateImageView.setOnClickListener { profilePictureClickHandler() }

        updateNameRelativeLayout.setOnClickListener { updateNameClickHandler() }

        updateProfilePictureButton.setOnClickListener { updateProfilePictureClickHandler() }
        imageChooserAction = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            println("UpdateProfilePictureActivityResultCode: ${it.resultCode}")

            if (it.resultCode == Activity.RESULT_OK) {
                updateImageView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                updateImageView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                updateImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                updateImageView.imageTintMode = null
                val profilePictureUri = it.data?.data

                try {
                    updateCurrentUserProfilePictureInDatabase(profilePictureUri)
                } catch (e: Exception) {
                    profilePictureUpdateFailed()
                }
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    override fun onStart() {
        super.onStart()
        updateProfileName.text = SharedPrefrencesUtil.getStringPrefrences(
            this, SharedPrefrencesConstant.REGISTERED_USER_NAME
        )

        Log.d("DP", "onStart() called")

        updateProfilePhone.text = getString(
            R.string.phone_number, SharedPrefrencesUtil.getStringPrefrences(
                this, SharedPrefrencesConstant.REGISTERED_MOBILE_NUMBER
            )
        )
        updateProfileAbout.text = SharedPrefrencesUtil.getStringPrefrences(
            this, SharedPrefrencesConstant.USER_PROFILE_STATUS
        )

        updateProfilePictureImageContent()
    }

    private fun updateProfilePictureImageContent() {
        if (currentUser.photoUrl != null && currentUser.photoUrl.toString() != "null") {
            println(currentUser.photoUrl)
            updateImageView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            updateImageView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            updateImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            updateImageView.imageTintMode = null
            Glide.with(this).load(currentUser.photoUrl).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(updateImageView)

        }
    }

    private fun updateCurrentUserProfilePictureInDatabase(profileImageUri: Uri?) {
        if (profileImageUri != null) {
            updateProfilePictureProgressBar.visibility = View.VISIBLE

            val storage = FirebaseStorage.getInstance()
            storage.maxUploadRetryTimeMillis = 18000
            val profilePictureRef =
                storage.reference.child("${FireBaseConstant.DIRECTORY_PATH_OF_PROFILE_PICTURE}/${currentUser.uid}")

            profilePictureRef.putFile(profileImageUri).addOnFailureListener {
                    Log.d(
                        ApplicationLoggingConstant.FIREBASE_PROFILE_PICTURE_UPLOAD_FAIL.toString(),
                        "Error uploading updated profile picture. \nCause: ${it.message}"
                    )

                    if (it.javaClass == StorageException::javaClass || it.javaClass == FirebaseNetworkException::javaClass || it.javaClass == UnknownHostException::javaClass) {
                        Toast.makeText(
                            this, "Error Connecting to server", Toast.LENGTH_SHORT
                        ).show()
                        updateProfilePictureProgressBar.visibility = View.INVISIBLE
                    } else {
                        profilePictureUpdateFailed()
                    }
                }.addOnCompleteListener { imageUploadTask ->
                    if (imageUploadTask.isSuccessful) {
                        val imageUploadTaskSnapshot = imageUploadTask.result
                        val uploadSessionUri = imageUploadTaskSnapshot.uploadSessionUri.toString()
                        val str1 =
                            uploadSessionUri.substring(0, uploadSessionUri.indexOf("&uploadType"))
                        val newProfilePictureUrl = "$str1&alt=media"
                        val profileUpdateRequest = UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(newProfilePictureUrl))

                        currentUser.updateProfile(profileUpdateRequest.build())
                            .addOnCompleteListener { currentUserUpdateTask ->
                                if (currentUserUpdateTask.isSuccessful) {
                                    val usersCollection = FirebaseFirestore.getInstance()
                                        .collection(FireBaseConstant.COLLECTIONS_OF_USERS)

                                    usersCollection.document(currentUser.uid).get()
                                        .addOnSuccessListener { userSnapshot ->
                                            val user = userSnapshot.toObject<User>()

                                            if (user != null) {
                                                user.getUid()?.let {
                                                    usersCollection.document(it).update(
                                                            "profilePictureUrl",
                                                            newProfilePictureUrl
                                                        )
                                                        .addOnCompleteListener { userProfilePictureUpdateTask ->
                                                            if (userProfilePictureUpdateTask.isSuccessful) {
                                                                Toast.makeText(
                                                                    this,
                                                                    "Profile Picture updated successfully",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                updateProfilePictureProgressBar.visibility =
                                                                    View.INVISIBLE
                                                                updateProfilePictureImageContent()
                                                            } else {
                                                                profilePictureUpdateFailed()
                                                            }
                                                        }.addOnFailureListener {
                                                            profilePictureUpdateFailed()
                                                        }.addOnCanceledListener {
                                                            profilePictureUpdateFailed()
                                                        }
                                                }
                                            } else {
                                                profilePictureUpdateFailed()
                                            }
                                        }.addOnFailureListener {
                                            profilePictureUpdateFailed()
                                        }.addOnCanceledListener {
                                            profilePictureUpdateFailed()
                                        }
                                } else {
                                    profilePictureUpdateFailed()
                                }
                            }.addOnFailureListener {
                                profilePictureUpdateFailed()
                            }.addOnCanceledListener {
                                profilePictureUpdateFailed()
                            }
                    } else if (imageUploadTask.exception != null) {
                        Log.d("DisplayPicture", imageUploadTask.exception?.message.toString())
                        profilePictureUpdateFailed()
                    }
                }
        }
    }

    private fun profilePictureUpdateFailed() {
        updateProfilePictureProgressBar.visibility = View.INVISIBLE
        Toast.makeText(
            this, "Error updating profile picture", Toast.LENGTH_SHORT
        ).show()

    }


    private fun updateNameClickHandler() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Enter your name")
        val view = layoutInflater.inflate(R.layout.update_user_name, null)
        val editText: EditText = view.findViewById(R.id.newNameInputEditText)
        editText.setText(
            SharedPrefrencesUtil.getStringPrefrences(
                this, SharedPrefrencesConstant.REGISTERED_USER_NAME
            )
        )
        editText.requestFocus()
        editText.selectAll()
        alertDialogBuilder.setView(view)

        alertDialogBuilder.setPositiveButton("Save") { dialog, _ ->
            val newName = editText.text.toString()
            if (newName.isEmpty()) {
                Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                if (currentUser.displayName != newName.trim()) {
                    updateUserName(newName.trim())
                }
                dialog.dismiss()
            }
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#4DAC9C"))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#4DAC9C"))
        }

        alertDialog.window?.setGravity(Gravity.BOTTOM)
        alertDialog.show()
    }

    private fun updateUserName(newName: String) {
        val profileUpdateRequest = UserProfileChangeRequest.Builder().setDisplayName(newName)

        currentUser.updateProfile(profileUpdateRequest.build()).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Name Updated Successfully", Toast.LENGTH_SHORT).show()
                Log.d(
                    ApplicationLoggingConstant.PROFILE_NAME_CHANGED.toString(),

                    "Profile Name updated"
                )
                SharedPrefrencesUtil.setStringPrefrences(
                    this, SharedPrefrencesConstant.REGISTERED_USER_NAME, newName
                )
                updateProfileName.text = SharedPrefrencesUtil.getStringPrefrences(
                    this, SharedPrefrencesConstant.REGISTERED_USER_NAME
                )

                FirebaseFirestore.getInstance().collection(FireBaseConstant.COLLECTIONS_OF_USERS)
                    .document(currentUser.uid).update("name", newName)
            }
        }
    }

    private fun updateProfilePictureClickHandler() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        val chooserIntent = Intent.createChooser(getIntent, "Select Profile Picture")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
        imageChooserAction.launch(chooserIntent)
    }


    private fun profilePictureClickHandler() {
        val profilePicture = currentUser.photoUrl.toString()

        if (profilePicture != "null") {
            val fullScreenProfilePictureIntent =
                Intent(this, FullScreenProfilePictureActivity::class.java)
            fullScreenProfilePictureIntent.putExtra(
                ApplicationConstant.FULL_SCREEN_PROFILE_PICTURE_TITLE, "Profile Photo"
            )
            fullScreenProfilePictureIntent.putExtra(
                ApplicationConstant.FULL_SCREEN_PROFILE_PICTURE_URL, profilePicture
            )
            startActivity(fullScreenProfilePictureIntent)
        } else {
            Toast.makeText(this, "No profile picture", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}