package com.example.kutta.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.kutta.MainActivity
import com.example.kutta.R
import com.example.kutta.databinding.ActivityLoginBinding
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    val auth =FirebaseAuth.getInstance()
    private lateinit var dialog: AlertDialog
    private var  verificationId :String?=null
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog = AlertDialog.Builder(this).setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()
        binding.sendotp.setOnClickListener {
            if (binding.userNumber.text!!.isEmpty()){
                binding.userNumber.error= "Please enter your number"


            }else{
                sendOtp(binding.userNumber.text.toString())
            }
        }

        binding.verifyotp.setOnClickListener {
            if (binding.userOTP.text!!.isEmpty()){
                binding.userOTP.error= "Please enter your number"


            }else{
                verifyOtp(binding.userOTP.text.toString())
            }
        }
    }

    private fun verifyOtp(otp: String) {
      //  binding.sendotp.showLoadingButton()
        dialog.show()
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun sendOtp(number: String) {
      //
        //  binding.sendotp.showLoadingButton()
        dialog.show()
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
      //      binding.sendotp.showNormalButton()
                dialog.dismiss()
           signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@LoginActivity.verificationId = verificationId
                dialog.dismiss()
        //        binding.sendotp.showNormalButton()
                binding.numberLaunch.visibility=GONE
                binding.OtpLayout.visibility= VISIBLE

            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
           //     binding.sendotp.showNormalButton()
                if (task.isSuccessful) {
                    checkUserExisting(binding.userNumber.text.toString())

                } else {
                    dialog.dismiss()
                    Toast.makeText(this,task.exception!! .message,Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserExisting(number: String) {
        FirebaseDatabase.getInstance().getReference("users").child("+91"+number)
            .addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity,p0.message,Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {


                    if (p0.exists()){
                        dialog.dismiss()
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
                        finish()
                    }
                }
            })

    }
}