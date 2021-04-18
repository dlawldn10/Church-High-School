package com.project.churchproject

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.main_toolbar.*


open class BasicActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    fun setBottomNavBar(){
        BottomNavi.setOnNavigationItemSelectedListener(this)
    }

    fun replaceFragment(fragment : Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val container = findViewById<LinearLayout>(R.id.Container)
        fragmentTransaction.replace(container.id, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }



    fun setView(n: Int){
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        when(n){
            1->{
                val fragHome = Frag_Home()
                fragmentTransaction.replace(Container.id, fragHome)
                fragmentTransaction.commit()


//                LogOutButton.setOnClickListener {
//                    logOut()
//                    finish()    //메인 액티비티 끝내기.
//                }
            }
            2->{
                val fragAttendance = Frag_Attendance()
                fragmentTransaction.replace(Container.id, fragAttendance)
                fragmentTransaction.commit()
            }
            3->{
                val fragContents = Frag_Contents()
                fragmentTransaction.replace(Container.id, fragContents)
                fragmentTransaction.commit()
            }
            4->{
                val fragMemo = Frag_MemoList()
                fragmentTransaction.replace(Container.id, fragMemo)
                fragmentTransaction.commit()
            }
            5->{
                val fragChat = Frag_ChatFriendsList()
                fragmentTransaction.replace(Container.id, fragChat)
                fragmentTransaction.commit()
            }
        }
    }

    fun setToolbar(){
        setSupportActionBar(main_layout_toolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setHomeAsUpIndicator(com.project.churchproject.R.drawable.ic_action_munu) // 홈버튼(툴바 왼쪽 끝 버튼) 이미지 변경
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게

        main_navigationView.setNavigationItemSelectedListener(this) //navigation 리스너
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.Mypage-> goToMypage()
            R.id.MyAttendance-> {
                Toast.makeText(this,"내 출석현황", Toast.LENGTH_SHORT).show()
            }
//            R.id.item3-> Toast.makeText(this,"item3 clicked", Toast.LENGTH_SHORT).show()

            R.id.Home-> {
                setView(1)
            }
            R.id.AttendanceCheck-> {
                setView(2)
            }
            R.id.Contents-> {
                setView(3)
            }
            R.id.Memo-> {
                setView(4)
            }
            R.id.Chat-> {
                setView(5)
//                startFriendsListActivity()
            }


        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{ // 메뉴 버튼
                main_drawer_layout.openDrawer(GravityCompat.START)    // 네비게이션 드로어 열기
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToMypage(){
        val intent2 = Intent(this, MemberInitUpdateActivity::class.java)
        startActivity(intent2)
    }


    private fun logOut(){
        //val currentUser = auth.currentUser
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }



}