package com.project.churchschool.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.ktx.Firebase
import com.project.churchschool.Activity.BasicActivity
import com.project.churchschool.DataClass.AttndnceData
import com.project.churchschool.DataClass.FinalAttndnceData
import com.project.churchschool.DataClass.MemberInfo
import com.project.churchschool.R
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.fragment_attndnce_chck.view.*
import kotlin.collections.ArrayList


class Teacher_AttndnceChckFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    val db = FirebaseFirestore.getInstance()
    var auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser

    val currentUser = db.collection("users").document(user?.email.toString())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_attndnce_chck, container, false)
        val selectedData : AttndnceData? = (activity as BasicActivity).getSelectedAttendnceData()
        (activity as BasicActivity).setSelectedAttendnceData(null)
        val Save_Bttn = rootView.findViewById<ImageView>(R.id.Attndnce_save_imageView)
        val Update_Bttn = rootView.findViewById<ImageView>(R.id.Attndnce_update_imageView)
        val Cancel_Bttn = rootView.findViewById<ImageView>(R.id.Attendnce_cancel_imageView)
        val Modify_Bttn = rootView.findViewById<ImageView>(R.id.modifyAttndnce_imageView)
        val Delete_Bttn = rootView.findViewById<ImageView>(R.id.deleteAttndnce_imageView)
        val Screen = rootView.findViewById<ImageView>(R.id.Screen)
        val profilePhoto = rootView.AttndnceChk_profileImage

        currentUser.get(Source.CACHE).addOnSuccessListener { documentSnapshot ->
            val currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
            Glide.with(this).load(currentUserInfo?.profilePhotoUrl).override(500).into(profilePhoto)
        }

        if(selectedData != null) {  //???????????? ????????? ??????UI
            Modify_Bttn.visibility = View.VISIBLE
            Delete_Bttn.visibility = View.VISIBLE
            Save_Bttn.visibility = View.GONE
            Update_Bttn.visibility = View.GONE
            Cancel_Bttn.visibility = View.GONE
            }
        else{
            //??????UI
            Modify_Bttn.visibility = View.GONE
            Delete_Bttn.visibility = View.GONE
            Update_Bttn.visibility = View.GONE
            Save_Bttn.visibility = View.VISIBLE
            Cancel_Bttn.visibility = View.VISIBLE
        }

        //????????? ??????
        loadData(rootView, selectedData, false)

        //?????? ??? ??????(????????????) ?????? ??????
        //?????? ????????? ??????
        Update_Bttn.setOnClickListener {
            //????????? ????????? ????????????
            currentUser.get(Source.CACHE).addOnSuccessListener { documentSnapshot ->
                val currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
                val TodayAttndnceData = AttndnceData(
                    (activity as BasicActivity).StudentsAttndnceData, 
                    selectedData?.date)

                (activity as BasicActivity).addAttndnceInfo(db, currentUserInfo, TodayAttndnceData)
            }

            //??????????????? ?????? 
            currentUser.get(Source.CACHE).addOnSuccessListener { documentSnapshot ->
                val currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
                db.collection("attendances")
                    .document(currentUserInfo.name.toString())
                    .update("attndnceDataList", FieldValue.arrayRemove(selectedData))
                    .addOnSuccessListener {

                    }
            }

        }
        
        //???????????? ??????
        //????????? ???????????? ??????
        Save_Bttn.setOnClickListener {
            //????????????
            currentUser.get().addOnSuccessListener { documentSnapshot ->
                val currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
                val TodayAttndnceData = AttndnceData(
                    (activity as BasicActivity).StudentsAttndnceData,
                    (activity as BasicActivity).getYYYYMMDD())

                (activity as BasicActivity).addAttndnceInfo(db, currentUserInfo, TodayAttndnceData)
            }
            
        }

        //???????????? ?????????
        Modify_Bttn.setOnClickListener {
            Modify_Bttn.visibility = View.GONE
            Delete_Bttn.visibility = View.VISIBLE
            Save_Bttn.visibility = View.GONE
            Update_Bttn.visibility = View.VISIBLE
            Cancel_Bttn.visibility = View.VISIBLE
            Screen.visibility = View.GONE


            //????????????.
            loadData(rootView, selectedData, true)
        }

        //???????????? ??????
        Delete_Bttn.setOnClickListener {
            if(selectedData != null) {
                //??????????????? ??????
                currentUser.get(Source.CACHE).addOnSuccessListener { documentSnapshot ->
                    val currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
                    makeAlert_delete("????????????", currentUserInfo, selectedData)
                }
            }

        }

        Cancel_Bttn.setOnClickListener {
            makeAlert_calncel("????????????")
        }


        return rootView
    }

    private fun makeAlert_delete(what : String, currentUserInfo: MemberInfo, selectedData: AttndnceData?){   //??? ???????????? ???????????? ????????????
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("$what ??????")
        builder.setMessage("??? $what" + "??? ?????? ???????????????????")
        builder.setPositiveButton("???") { dialogInterface: DialogInterface, i: Int ->
            deleteData(currentUserInfo, selectedData)

        }
        builder.setNegativeButton("?????????") { dialogInterface: DialogInterface, i: Int ->

        }
        builder.show()

    }

    fun makeAlert_calncel(what : String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("$what ??????")
        builder.setMessage("??? $what" + "????????? ?????? ???????????????????")
        builder.setPositiveButton("???") { dialogInterface: DialogInterface, i: Int ->
            (activity as BasicActivity).setView(2)
        }
        builder.setNegativeButton("?????????") { dialogInterface: DialogInterface, i: Int ->

        }
        builder.show()
    }

    fun deleteData(currentUserInfo : MemberInfo, selectedData : AttndnceData?){
        db.collection("attendances")
            .document(currentUserInfo.name.toString())
            .update("attndnceDataList", FieldValue.arrayRemove(selectedData))
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(), "??????????????? ?????? ???????????????.",
                    Toast.LENGTH_SHORT
                ).show()
                (activity as BasicActivity).setView(2)
            }
    }



    fun loadData(rootView : View, selectedData: AttndnceData?, isModifyMode : Boolean){
        var studentList = arrayListOf<String>()
        
        currentUser.get().addOnSuccessListener { documentSnapshot ->
            val currentUserInfo = documentSnapshot.toObject(MemberInfo::class.java)!!
            if(selectedData != null) {
                rootView.findViewById<TextView>(R.id.attendanceChckDate).text = selectedData.date
            }else{
                rootView.findViewById<TextView>(R.id.attendanceChckDate).text = (activity as BasicActivity).getYYYYMMDD()

            }
            rootView.findViewById<TextView>(R.id.Teaher_MyName).text = currentUserInfo.name + " " + currentUserInfo.group

            db.collection("classes")
                .document("2021").get()
                .addOnSuccessListener { document ->

                    studentList = document.data?.get(currentUserInfo?.name) as ArrayList<String>

                    viewManager = LinearLayoutManager(requireContext())
                    if(selectedData != null){
                        viewAdapter = AttndnceCheck_StudentListAdapter(studentList, activity, this, selectedData, isModifyMode)
                    }else{
                        viewAdapter = AttndnceCheck_StudentListAdapter(studentList, activity, this, null, isModifyMode)
                    }

                    recyclerView =
                        rootView.findViewById<RecyclerView>(R.id.Teaher_check_Attndnce_recycler_view).apply {
                            setHasFixedSize(true)
                            layoutManager = viewManager
                            adapter = viewAdapter
                            //??????????????? ????????? ????????????.
                        }

                }

        }
    }


}