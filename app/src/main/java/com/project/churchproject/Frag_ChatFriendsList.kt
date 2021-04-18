package com.project.churchproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class Frag_ChatFriendsList : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    lateinit var rootView : View
    var myName : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val db = FirebaseFirestore.getInstance()
        var auth: FirebaseAuth
        var userList = arrayListOf<String>()
        rootView = inflater.inflate(R.layout.frag_chat, container, false)

        auth = Firebase.auth
        val user = auth.currentUser


        db.collection("users")  //데이터베이스에서 유저 리스트 뽑아오기
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {

                    if (document.id != user?.uid.toString()) {  //가져오는 문서 id들 중에 현재 로그인된 유저와 같은 아이디를 갖는 문서는 이름을 갖고오지 않는다.
                        userList.add(document.data["name"].toString())
                    }


                }

                viewManager = LinearLayoutManager(requireContext())
                viewAdapter = FragChatFriendsAdapter(userList, context)//여기 문제..


                recyclerView =
                    rootView.findViewById<RecyclerView>(R.id.frag_chat_recycler_view).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                        //뷰메니저와 어댑터 적용하기.
                    }

            }

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    val intent = activity?.intent
                    myName = intent?.getStringExtra("CURRENTUSER_NAME")
                    Log.e("인텐트", myName.toString())
                }
            }
        }
    }








}