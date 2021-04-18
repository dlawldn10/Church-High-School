package com.project.churchproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Frag_MemoList : Fragment(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.frag_memo, container, false)

        val db = FirebaseFirestore.getInstance()
        var memoList = arrayListOf<MemberMemo>()
        var auth: FirebaseAuth = Firebase.auth
        val user = auth.currentUser


        db.collection("memos")
            .get()
            .addOnSuccessListener { result -> //데이터베이스에서 메모 리스트 뽑아오기

                for (document in result) {


                    if (document.data["id"] == user?.uid.toString()) {  //가져오는 문서 id들 중에 현재 로그인된 유저와 같은 아이디를 갖고있는 문서만 불러온다
                        memoList.add(document.toObject(MemberMemo::class.java))
                    }


                }

                viewManager = LinearLayoutManager(requireContext())
                viewAdapter = MemoListAdapter(memoList, activity)


                recyclerView =
                    rootView.findViewById<RecyclerView>(R.id.Memo_List_recycler_view).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                        //뷰메니저와 어댑터 적용하기.
                    }

            }


        rootView.findViewById<FloatingActionButton>(R.id.addMemoButton).setOnClickListener {    //메모 추가하기 버튼

            val fragPostMemo = Frag_PostMemo()
            (activity as BasicActivity).replaceFragment(fragPostMemo)

        }
        return rootView
    }


}