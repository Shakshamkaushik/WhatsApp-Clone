package com.example.whatsappclone.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.R
import com.example.whatsappclone.constants.SharedPrefrencesConstant
import com.example.whatsappclone.utils.GlobalLog
import com.example.whatsappclone.utils.SharedPrefrencesUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class VerifyOtpActivity : AppCompatActivity() {
    private lateinit var otpInputs: ArrayList<EditText>
    private lateinit var verifyMobileNumber: TextView
    private lateinit var verifyNumber: TextView
    private lateinit var wrongNumber: TextView
    private lateinit var resendOtp: TextView
    private lateinit var callForOtp: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    private lateinit var OTP: String
    private lateinit var phoneNumber: String
    private lateinit var countryCode: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private val TAG: String = VerifyOtpActivity::class.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        OTP = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("phoneNumber")!!
        countryCode = intent.getStringExtra("countryCode")!!

        init()
    }

    override fun onResume() {
        super.onResume()
        verifyNumber.text = getString(R.string.verify_phone_header_text, phoneNumber)
        verifyMobileNumber.text =
            getString(R.string.phone_number_ongoing_verification, phoneNumber)
    }


    private fun init() {
        otpInputs = ArrayList()
        otpInputs.add(findViewById(R.id.etOtp1))
        otpInputs.add(findViewById(R.id.etOtp2))
        otpInputs.add(findViewById(R.id.etOtp3))
        otpInputs.add(findViewById(R.id.etOtp4))
        otpInputs.add(findViewById(R.id.etOtp5))
        otpInputs.add(findViewById(R.id.etOtp6))
        auth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.verifyOtpProgressBar)
        verifyNumber = findViewById(R.id.tvVerifyNumber)
        verifyMobileNumber = findViewById(R.id.tvMobileNumberUndergoingVerification)
        wrongNumber = findViewById(R.id.wrongNumberTextView)
        resendOtp = findViewById(R.id.tvResendBtn)
        callForOtp = findViewById(R.id.tvCallMe)

        wrongNumber.setOnClickListener {
            startActivity(Intent(this, EnterPhoneNumberActivity::class.java))
            finish()
        }
        resendOtp.setOnClickListener {
            resendOTP(this, phoneNumber)
        }
        callForOtp.setOnClickListener {
            Toast.makeText(this, "This feature not available right now! ", Toast.LENGTH_SHORT)
                .show()
        }

        otpInputs.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (editText.text.toString().length == 1 && index < otpInputs.size - 1) {
                        editText.clearFocus()
                        otpInputs[index + 1].requestFocus()
                        otpInputs[index + 1].isCursorVisible = true
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun afterTextChanged(p0: Editable?) {
                    if (index == otpInputs.size - 1) {
                        verifyOtpProcess()
                    }

                    if (editText.text.toString().isEmpty() && index > 0) {
                        editText.clearFocus()
                        otpInputs[index - 1].requestFocus()
                        otpInputs[index - 1].isCursorVisible = true
                    }
                }
            })
        }
    }

    //reseend OTP
    fun resendOTP(activity: Activity, mobileNo: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(mobileNo) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken) // ForceResendingToken from callbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @RequiresApi(Build.VERSION_CODES.O)
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
                    this@VerifyOtpActivity,
                    "Internet Connectivity error",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@VerifyOtpActivity,
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

            resendToken = token
            progressBar.visibility = View.GONE
            GlobalLog.showLog("TAG", "onCodeSent:$verificationId")


//            val intent = Intent(this@VerifyOtpActivity, VerifyOtpActivity::class.java)
//            intent.putExtra("OTP", verificationId)
//            intent.putExtra("resendToken", token)
//            intent.putExtra("phoneNumber", ph)
//            intent.putExtra("countryCode", countryCode.text.toString().substring(1))
//            startActivity(intent)


            // Save verification ID and resending token so we can use them later

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun verifyOtpProcess() {
        progressBar.visibility = View.VISIBLE
        val otp = fetchInputOtp(otpInputs)
        val isOtpValid: Boolean = otpInputs.size == otp.length

        if (isOtpValid) {
            val verificationCredentials = PhoneAuthProvider.getCredential(OTP, otp)
            signInWithPhoneAuthCredential(verificationCredentials)
        } else {
            Toast.makeText(this, "OTP should be of length 6", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    SharedPrefrencesUtil.setStringPrefrences(
                        this,
                        SharedPrefrencesConstant.REGISTERED_COUNTRY_CODE,
                        countryCode
                    )
                    SharedPrefrencesUtil.setStringPrefrences(
                        this,
                        SharedPrefrencesConstant.REGISTERED_MOBILE_NUMBER,
                        phoneNumber
                    )
                    // Sign in success, update UI with the signed-in user's information
                    GlobalLog.showLog(TAG, "signInWithCredential:success")
                    Toast.makeText(this, "Authentication Sucessful", Toast.LENGTH_SHORT).show()
                    val profileInfoActivityIntent =
                        Intent(this, ProfileInfoSetupActivity::class.java)
                    startActivity(profileInfoActivityIntent)
                    finish()

                } else {
                    progressBar.visibility = View.GONE
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                        otpInputs.forEach {
                            it.text = Editable.Factory.getInstance().newEditable("")
                            it.clearFocus()
                        }
                        otpInputs[0].requestFocus()
                        otpInputs[0].isCursorVisible = true
                    } else {
                        println(task.exception?.javaClass)
                        println(task.exception?.message)
                        Toast.makeText(this, "Something wrong happened", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }


    private fun fetchInputOtp(otpInputs: ArrayList<EditText>): String {
        val otpBuilder = StringBuilder(otpInputs.size)

        otpInputs.forEach { editText ->
            otpBuilder.append(editText.text.toString().trim())
        }

        return otpBuilder.toString()
    }


}