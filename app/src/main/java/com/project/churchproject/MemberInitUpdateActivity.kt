package com.project.churchproject

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_update_member_init.*
import kotlinx.android.synthetic.main.item_gallery.*

class MemberInitUpdateActivity : AppCompatActivity() {
    private val TAG : String = "MemberInitActivity"
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var profilePhotoUrl : String? = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_member_init)
        
        // Initialize Firebase Auth
        auth = Firebase.auth
        val user = auth.currentUser

        //여기서부터
        val currentUser = db.collection("users").document(user?.uid.toString())
        currentUser.get().addOnSuccessListener { documentSnapshot ->
            val currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)
            NameEditText.setText(currentUserInfo?.name)
            GroupEditText.setText(currentUserInfo?.group)
            EmailEditText_MemUpdate.setText(currentUserInfo?.email)
            PhoneEditText.setText(currentUserInfo?.phone)
            BirthEditText.setText(currentUserInfo?.birth)
            PostalAddressEditText.setText(currentUserInfo?.postalAddress)
            Glide.with(this).load(currentUserInfo?.profilePhotoUrl).centerCrop().override(500).into(ProfilePhoto)

            val resultIntent = Intent()
            resultIntent.putExtra("CURRENTUSER_NAME", currentUserInfo?.name)
            setResult(Activity.RESULT_OK, resultIntent)
        }

//        user?.let {
//            val email = user.email
//            EmailEditText_MemUpdate.setText(email)
//
//        }
        //여기까지 -> 데이터 불러와서 출력하기

        //수정하기 버튼 눌렀을 때
        ModifyBttn.setOnClickListener { 
            onModifyBttnClick()
            
        }

        //완료 버튼 눌렀을 때
        UpdateBttn.setOnClickListener {
            onUpdateBttnClick()
        }


        //프로필 사진 눌렀을 때
        ProfilePhoto.setOnClickListener {
            //갤러리 보여주기

            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {     //권한이 있을 때
                    // You can use the API that requires the permission.
                    //                        performAction()
                    //페이지 이동
                    goToGallery()
                }
                else -> {   //권한이 없을때
                    // You can directly ask for the permission.
                    //권한 물어보기

                    requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                    )

                }

            }


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {

            val result = data?.getStringExtra("PROFILE_PHOTO_PATH")
            profilePhotoUrl = result
            Glide.with(this).load(result).centerCrop().override(500).into(ProfilePhoto)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun goToGallery(){
        val intent = Intent(this, GalleryActivity::class.java)
        intent.putExtra("from", "MemberInintActivity")
        startActivityForResult(intent, 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    goToGallery()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(
                        baseContext, "프로필 사진을 변경하려면 권한이 필요합니다./n" +
                                "'설정>앱>문화교회 고등부>권한' 에서 설정해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                return
            }

        }
    }

    override fun onStart() {
        super.onStart()
        ProfilePhoto.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        if(ModifyBttn.isEnabled){
            ProfilePhoto.isEnabled = false
        }
        else{
            ProfilePhoto.isEnabled = true
        }
    }

    //수정하기 버튼을 누르면
    private fun onModifyBttnClick(){
        UpdateBttn.isEnabled = true
        ModifyBttn.isEnabled = false
        
        ProfilePhoto.isEnabled = true
        
        NameEditText.isEnabled = true
        //group그룹은 수정 불가
        EmailEditText_MemUpdate.isEnabled = true
        PhoneEditText.isEnabled = true
        BirthEditText.isEnabled = true
        PostalAddressEditText.isEnabled = true
    }

    //완료버튼을 누르면
    private fun onUpdateBttnClick(){
        UpdateBttn.isEnabled = false
        ModifyBttn.isEnabled = true

        ProfilePhoto.isEnabled = false

        NameEditText.isEnabled = false
        EmailEditText_MemUpdate.isEnabled = false
        PhoneEditText.isEnabled = false
        BirthEditText.isEnabled = false
        PostalAddressEditText.isEnabled = false

        val user = auth.currentUser
        //이메일 auth는 별도 업데이트
        user!!.updateEmail(EmailEditText_MemUpdate.text.toString())
        //나머지 업데이트
        val memberInfo = MemberInfo(NameEditText.text.toString(), GroupEditText.text.toString(), EmailEditText_MemUpdate.text.toString(), PhoneEditText.text.toString(),
            BirthEditText.text.toString(), PostalAddressEditText.text.toString(), profilePhotoUrl)

        db.collection("users").document(user?.uid.toString()).set(memberInfo).addOnCompleteListener {
            if (it.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                    Toast.makeText(
                        baseContext, "업데이트가 완료 되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            else{
                Log.d(TAG, "User profile updated.")
                Toast.makeText(
                    baseContext, "업데이트 실패",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

//        val profileUpdates = userProfileChangeRequest {
//            displayName = NameEditText.text.toString()
//            val displayPhone = PhoneEditText.text.toString()
//            val displayBirth = BirthEditText.text.toString()
//            val displayPostalAdress = PostalAddressEditText.text.toString()
//
//
////            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
//        }

//        //프로필(이름) 업데이트
//        user!!.updateProfile(profileUpdates)
//            .addOnCompleteListener { task ->
//
//                if (task.isSuccessful) {
//                    Log.d(TAG, "User profile updated.")
//                    Toast.makeText(
//                        baseContext, "업데이트가 완료 되었습니다.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                }
//            }



    }
}