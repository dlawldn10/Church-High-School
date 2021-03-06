package com.project.churchschool.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Source
import com.google.firebase.ktx.Firebase
import com.project.churchschool.DataClass.AttndnceData
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.DataClass.MemoData
import com.project.churchschool.Fragment.*
import com.project.churchschool.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.main_drawer_header.*
import kotlinx.android.synthetic.main.main_drawer_header.view.*
import kotlinx.android.synthetic.main.main_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

open class BasicActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    var StudentsAttndnceData : MutableMap<String, Boolean>? = mutableMapOf()
    var SelectedMemo : MemoData? = null
    var SelectedAttndnce : AttndnceData? = null
    var myDBname = ""

    private val db = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = Firebase.auth
    private val user = auth.currentUser
    val currentUser = db.collection("users").document(user?.email.toString())
    var currentUserInfo : MemberInfo? = null


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings     //?????????????????? ?????? ???????????? ???????????? ??????.


    }

    fun getUser() : FirebaseUser? {
        return user
    }

    fun setBottomNavBar(){
        val navView: BottomNavigationView = findViewById(R.id.BottomNavi)
        navView.setOnNavigationItemSelectedListener(this)

    }


    fun setView(n: Int){
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        when(n){
            1->{
                val fragHome = HomeFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragHome)
                fragmentTransaction.commit()
            }
            2->{
                val fragMyClassAttendance = MyclassAttendanceFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragMyClassAttendance)
                fragmentTransaction.commit()
            }
            3->{
                val fragContents = ContentsFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragContents)
                fragmentTransaction.commit()
            }
            4->{
                val fragMemo = MemoFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragMemo)
                fragmentTransaction.commit()
            }
            5->{
                val fragPrayer = PrayerListFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragPrayer)
                fragmentTransaction.commit()
            }
            6->{
                val TchrFragAttndnce = Teacher_AttndnceChckFragment()
                fragmentTransaction.replace(Container_frameLayout.id, TchrFragAttndnce)
                fragmentTransaction.commit()
            }
            7->{
                val fragPostMemo = MemoPostFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragPostMemo)
                fragmentTransaction.commit()
            }
            8->{
                val fragPostPrayer = PrayerPostFragment()
                fragmentTransaction.replace(Container_frameLayout.id, fragPostPrayer)
                fragmentTransaction.commit()
            }
        }
    }


    fun setSelectedMemoData(data : MemoData?){
        SelectedMemo = data
    }

    fun getSelectedMemoData() : MemoData? {
        return SelectedMemo
    }

    fun setSelectedAttendnceData(data : AttndnceData?){
        SelectedAttndnce = data
    }

    fun getSelectedAttendnceData() : AttndnceData? {
        return SelectedAttndnce
    }

    fun set_myDBname(name : String){
        myDBname = name
    }

    fun get_myDBname() : String{
        return myDBname
    }

    fun setToolbar(){
        setSupportActionBar(main_layout_toolbar) // ????????? ??????????????? ????????? ??????
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24) // ?????????(?????? ?????? ??? ??????) ????????? ??????
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ???????????? ?????? ??? ?????? ?????????
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true) // ????????? ????????? ?????????

        main_navigationView.setNavigationItemSelectedListener(this) //navigation ?????????
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //??????
            R.id.Mypage -> gotoMypageActivity(this)

            R.id.StudentList -> Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show()

            R.id.TeacherList -> Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show()

            R.id.LogOut -> logOut()



            //?????????
            R.id.Home -> {
                setView(1)
            }
            R.id.AttendanceCheck -> {
                setView(2)
            }
            R.id.Contents -> {
                setView(3)
            }
            R.id.Memo -> {
                setView(4)
            }
            R.id.PrayerList -> {
                setView(5)
            }

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{ // = ?????? ??????
                main_drawer_layout.openDrawer(GravityCompat.START)    // ??????????????? ????????? ??????
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut(){
        FirebaseAuth.getInstance().signOut()
        gotoLogInActivity(this)
        Toast.makeText(this, "???????????? ???????????????", Toast.LENGTH_SHORT).show()
        finish()
    }


    fun GroupCheck_SignUp() : Boolean{     //??????/?????? ?????? ??? ????????? ??????????????? ??????.
        var result :Boolean = false
        if(StudentRadioBttn_Signup.isChecked || TeacherRadioBttn_Signup.isChecked){
            result = true
        }

        return result
    }

    fun GroupCheck_Login() : Boolean{     //??????/?????? ?????? ??? ????????? ??????????????? ??????.
        var result :Boolean = false
        if(StudentRadioBttn_Login.isChecked || TeacherRadioBttn_Login.isChecked){
            result = true
        }

        return result
    }

    //MyPage?????? ????????? ?????? : ?????? ????????? ??? ?????? ?????? ????????????
    fun loadCurrentUserInfo_toMypage(from : Source) {
        val user = auth.currentUser
        val currentUser = db.collection("users").document(user?.email.toString()) //?????? ??????????????? ?????? ??????
        currentUser.get(from).addOnSuccessListener { documentSnapshot ->
            currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)
            NameEditText.setText(currentUserInfo?.name)
            GroupEditText.setText(currentUserInfo?.group)
            EmailEditText_MemUpdate.setText(currentUserInfo?.email)
            PasswordEditText_Mypage.setText(currentUserInfo?.password)
            PhoneEditText.setText(currentUserInfo?.phone)
            BirthEditText.setText(currentUserInfo?.birth)
            PostalAddressEditText.setText(currentUserInfo?.postalAddress)
            Glide.with(this).load(currentUserInfo?.profilePhotoUrl).override(500).into(ProfilePhoto)
        }

    }


    fun setDrawer(headerView : View){
        currentUser.get().addOnSuccessListener { documentSnapshot ->
            currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)
            headerView.drawer_Name.setText(currentUserInfo?.name + " " + currentUserInfo?.group)
            headerView.drawer_email.setText(currentUserInfo?.email)
            Glide.with(this).load(currentUserInfo?.profilePhotoUrl).override(500).into(headerView.header_icon)
        }
    }


    //???????????? ??? ???????????? ???????????? ??? ??????
    fun setMemberInfo(memberInfo : MemberInfo, activityName: String){
        val user = auth.currentUser
        val currentUser = db.collection("users").document(memberInfo.email.toString())
        currentUser.set(memberInfo).addOnSuccessListener {

            Log.e("??????", "db?????? ??????")
            if(activityName.equals("MyPageActivity")){
                Toast.makeText(
                    baseContext, "??????????????? ?????????????????????.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if(activityName.equals("SignUpActivity")){
                Toast.makeText(
                    baseContext, "2222",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }   //db??? ?????? ??????.
    }

    //???????????? ???????????? ??????
    fun saveAttndnceData(name : String, Attndnce : Boolean){
        this.StudentsAttndnceData?.put(name, Attndnce)

    }

    //???????????? ???????????? ??? ??????
    fun addAttndnceInfo(db : FirebaseFirestore, currentUserInfo : MemberInfo, FinalattndnceData : AttndnceData){

        db.collection("attendances")
            .document(currentUserInfo.name.toString())
            .update("attndnceDataList", FieldValue.arrayUnion(FinalattndnceData))
            .addOnSuccessListener {

                Log.e("??????", "db?????? ??????")
                Toast.makeText(
                    baseContext, "??????????????? ???????????? ???????????????.",
                    Toast.LENGTH_SHORT
                ).show()
                setView(2)
        }   //db??? ?????? ??????.

    }

    fun convertTime(): Date {
        val now : Long = System.currentTimeMillis() //???????????? ????????????
        val mDate = Date(now)   //Date???????????? ?????????
        return mDate
    }

    fun getYear() : String{
        val year = SimpleDateFormat("YYYY")
        val getyear: String = year.format(convertTime())
        return getyear
    }

    fun getMonth(): String {
        val month = SimpleDateFormat("MM")     //?????????????????? ?????? ??????
        val getmonth: String = month.format(convertTime())
        return getmonth
    }

    fun getDate() : String{
        val date = SimpleDateFormat("DD")     //?????????????????? ?????? ??????
        val getdate: String = date.format(convertTime())
        return getdate
    }

    fun getYYYYMMDD() : String{
        val dateFormat = SimpleDateFormat("YYYY/MM/DD")
        val getYYYYMMDD_Date : String = dateFormat.format(convertTime())
        return getYYYYMMDD_Date

    }

    fun getYYYMM() : String{
        val dateFormat = SimpleDateFormat("YYYY/MM/")
        val getYYYYMM_Date : String = dateFormat.format(convertTime())
        return getYYYYMM_Date

    }


    //???????????? ?????? ??????
    fun gotoMainActivity(activity : Activity){
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    fun gotoSignUpActivity(activity : Activity){
        val intent = Intent(activity, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun gotoLogInActivity(activity : Activity){
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    fun gotoPasswordRecreateActivity(activity : Activity){
        val intent = Intent(activity, PasswordRecreateActivity::class.java)
        startActivity(intent)
    }

    fun gotoMypageActivity(activity : Activity){
        val intent = Intent(activity, MyPageActivity::class.java)
        startActivity(intent)
    }

    fun gotoGalleryActivity(activity : Activity){
        val intent = Intent(activity, GalleryActivity::class.java)
        startActivity(intent)
    }

}