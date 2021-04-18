package com.project.churchproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_password_recreate.*

class PasswordRecreateActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG : String = "LogInActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_recreate)

        // Initialize Firebase Auth
        auth = Firebase.auth

        PasswordRecreateBttn.setOnClickListener {
            recreatePassword()

        }
    }





    private fun recreatePassword(){
        val emailAddress = EmailAddressEditText_RecreatePassword.text.toString()

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    Toast.makeText(
                        baseContext, "입력하신 주소로 이메일이 발송되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


    }


}