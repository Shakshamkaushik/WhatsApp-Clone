package com.example.whatsappclone.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.R
import com.example.whatsappclone.utils.GlobalLog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class EnterPhoneNumberActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var btnSendOtp: Button
    private lateinit var phoneNumber: EditText
    private lateinit var countryCode: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var number: String
    private lateinit var countrySelectorSpinner: Spinner
    private lateinit var progressBar: ProgressBar
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_phone_number)
        init()


        btnSendOtp.setOnClickListener {
            number = phoneNumber.text.toString().trim()

            if (countryCode.text.toString().trim() != "+91") {
                Toast.makeText(this, "Not Available for you right now", Toast.LENGTH_SHORT).show()
            }

            when {
                number.isEmpty() -> {
                    val alertDialog = AlertDialog.Builder(this).setTitle("Error")
                        .setMessage("Please Enter Phone Number")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.create()
                    alertDialog.setOnShowListener {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(resources.getColor(R.color.chat_title_text_color, theme))
                    }
                    alertDialog.show()
                }

                number.length != 10 -> {
                    val alertDialog = AlertDialog.Builder(this).setTitle("Error")
                        .setMessage("Please Enter Phone Number")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.create()
                    alertDialog.setOnShowListener {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(resources.getColor(R.color.chat_title_text_color, theme))
                    }
                    alertDialog.show()
                }

                else -> {
                    number = "+${
                        countryCode.text.toString().substring(1)
                    }${phoneNumber.text}"
                    GlobalLog.showLog("TAG", "+${countryCode.text.toString().substring(1)}")
                    GlobalLog.showLog("TAG", "${phoneNumber.text}")

                    progressBar.visibility = View.VISIBLE
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }
            }
//            if (number.isNotEmpty()) {
//                if (number.length == 10) {
//                    number = "+${
//                        countryCode.text.toString().substring(1)
//                    }${phoneNumber.text}"
//                    Log.d("TAG", "+${countryCode.text.toString().substring(1)}")
//                    Log.d("TAG", "${phoneNumber.text}")
//
//                    progressBar.visibility = View.VISIBLE
//                    val options = PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(number) // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this) // Activity (for callback binding)
//                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
//                        .build()
//                    PhoneAuthProvider.verifyPhoneNumber(options)
//                } else {
//                    Toast.makeText(this, "Enter Correct Number", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(this, "Enter the Number", Toast.LENGTH_SHORT).show()
//            }
        }
    }
    private fun init() {
        btnSendOtp = findViewById(R.id.btnNext)
        phoneNumber = findViewById(R.id.etPhoneNumber)
        countryCode = findViewById(R.id.etCountryCode)
        progressBar = findViewById(R.id.verifyNumberProgressBar)
        countrySelectorSpinner = findViewById(R.id.countryCode_picker_enter_phone)
        progressBar.visibility = View.GONE
        auth = FirebaseAuth.getInstance()

        ArrayAdapter.createFromResource(
            this,
            R.array.countries_name,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            countrySelectorSpinner.adapter = adapter
        }
        countrySelectorSpinner.onItemSelectedListener = this

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    GlobalLog.showLog("TAG", "signInWithCredential:success")
                    Toast.makeText(this, "Authentication Sucessful", Toast.LENGTH_SHORT).show()


                } else {
                    // Sign in failed, display a message and update the UI
                    GlobalLog.showLog(
                        "TAG",
                        "signInWithCredential:failure, ${task.exception.toString()}"
                    )
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            GlobalLog.showLog("TAG", "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            GlobalLog.showLog("TAG", "onVerificationFailed ${e.message.toString()}")

            if (e is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(
                    this@EnterPhoneNumberActivity,
                    "Internet Connectivity error",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@EnterPhoneNumberActivity,
                    "Something wrong happened",
                    Toast.LENGTH_SHORT
                ).show()
            }
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            resendingToken = token
            progressBar.visibility = View.GONE
            btnSendOtp.isEnabled = true
            GlobalLog.showLog("TAG", "onCodeSent:$verificationId")


            val intent = Intent(this@EnterPhoneNumberActivity, VerifyOtpActivity::class.java)
            intent.putExtra("OTP", verificationId)
            intent.putExtra("resendToken", token)
            intent.putExtra("phoneNumber", number)
            intent.putExtra("countryCode", countryCode.text.toString().substring(1))
            startActivity(intent)


            // Save verification ID and resending token so we can use them later

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(
                Intent(
                    this@EnterPhoneNumberActivity,
                    ProfileInfoSetupActivity::class.java
                )
            )
            finish()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p2) {
            0 -> {
                countryCode.text =
                    Editable.Factory.getInstance()
                        .newEditable(getString(R.string.enter_phone_country_code, 91))
            }

            1 -> {
                countryCode.text =
                    Editable.Factory.getInstance()
                        .newEditable(getString(R.string.enter_phone_country_code, 971))
            }

            2 -> {
                countryCode.text = Editable.Factory.getInstance()
                    .newEditable(getString(R.string.enter_phone_country_code, 1))
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}