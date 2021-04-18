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

class LogInActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG : String = "LogInActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // Initialize Firebase Auth
        auth = Firebase.auth
        if(auth.currentUser != null){
            goToMainActivity()
        }


        GoSignUpPage.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }
        LogInButton.setOnClickListener {
            logIn()
        }

        PasswordReCreateBttn.setOnClickListener {
            val intent2 = Intent(this, PasswordRecreateActivity::class.java)
            startActivity(intent2)
        }

//        StudentRadioBttn.setOnClickListener {
//
//        }
//
//        TeacherRadioBttn.setOnClickListener {
//
//        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            finish()    //로그인 상태로 메인화면에서 뒤로가기 눌렀을 때 앱 끝내기.
        }
    }

    private fun goToMainActivity(){
        val intent2 = Intent(this, MainActivity::class.java)
        startActivity(intent2)
    }

    private fun logIn(){
        var email : String = EmailEditText_LogIn.text.toString()
        var password : String = PasswordEditText_Login.text.toString()

        if(email.isEmpty() || password.isEmpty() ||(!StudentRadioBttn.isChecked && !TeacherRadioBttn.isChecked)) {
            Toast.makeText(
                baseContext, "정보를 모두 입력하여 주십시오.",
                Toast.LENGTH_SHORT
            ).show()
        }else{

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {    //성공했을 때
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(
                                baseContext, "로그인되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val user = auth.currentUser
                            goToMainActivity()
                            finish()    //로그인 화면 끝내기

                            //updateUI(user)
                        } else {    //실패했을 때
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "이메일 또는 비밀번호가 일치하지 않습니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        // ...
                    }
                }


    }


}