package com.project.churchproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.frag_chat.*
import kotlinx.android.synthetic.main.frag_home.*
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.main_toolbar.*

class MainActivity : BasicActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        setToolbar()
        setBottomNavBar()
        setView(1)      //첫화면 홈화면으로.

        // Initialize Firebase Auth
        auth = Firebase.auth
        val user = auth.currentUser

    }

    override fun onBackPressed() { //뒤로가기 처리
        if(main_drawer_layout.isDrawerOpen(GravityCompat.START)){
            main_drawer_layout.closeDrawers()
            // 테스트를 위해 뒤로가기 버튼시 Toast 메시지
            Toast.makeText(this,"back btn clicked",Toast.LENGTH_SHORT).show()
        } else{
            super.onBackPressed()
        }
    }




}