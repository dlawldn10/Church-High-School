package com.project.churchproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_update_member_init.*
import java.lang.Exception

class SignUpActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG : String = "SignUpActivity"
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Auth
        auth = Firebase.auth


        SignUpButton.setOnClickListener{    //회원가입 버튼을 눌렀을 때
            signUp()

        }


    }


    private fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun CheckGroup() : String{
        var group:String = ""

        if(StudentRadioBttn_Signup.isChecked) {
            group = "학생"
        }else if(TeacherRadioBttn_Signup.isChecked){
            group = "교사"
        }

        return group
    }

    private fun isGroupChecked() : Boolean{
        var result :Boolean = false
        if(StudentRadioBttn_Signup.isChecked || TeacherRadioBttn_Signup.isChecked){
            result = true
        }
        return result
    }

    private fun signUp(){

        var email : String = EmailEditText.text.toString()
        var password : String = PasswordEditText.text.toString()
        var passwordCheck : String = PasswordCheckEditText.text.toString()

        if(email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty() || !isGroupChecked()) {
            Toast.makeText(
                baseContext, "정보를 모두 입력하여 주십시오.",
                Toast.LENGTH_SHORT
            ).show()
        }else{

            if (!password.equals(passwordCheck)) {  //비밀번호 일치 체크
                Toast.makeText(
                    baseContext, "비밀번호가 일치하지 않습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {    //비밀번호도 일치하면..

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        val user = auth.currentUser
                        val memberInfo = MemberInfo(null, CheckGroup(), EmailEditText.text.toString(),
                            null, null)

                        db.collection("users").document(user?.uid.toString()).set(memberInfo)


                        if (task.isSuccessful) {    //성공했을 때
                            // Sign in success, update UI with the signed-in user's information


                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(
                                baseContext, "회원가입이 정상적으로 완료되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            goToMainActivity()
                            finish()

                        } else {    //실패했을 때
                            // If sign in fails, display a message to the user.
                            val errorType1: String =
                                "The email address is already in use by another account."
                            val errorType2: String =
                                "The given password is invalid. [ Password should be at least 6 characters ]"

                            val error: Exception? = task.exception
                            Log.w(TAG, "createUserWithEmail:failure", error)

                            if (error?.message.toString() == errorType1) {
                                Toast.makeText(
                                    baseContext, "동일한 이메일이 이미 존재합니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (error?.message.toString() == errorType2) {
                                Toast.makeText(
                                    baseContext, "비밀번호는 최소 6자리 이상이어야 합니다.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }


                        // ...


                    }
            }
            }

    }


}