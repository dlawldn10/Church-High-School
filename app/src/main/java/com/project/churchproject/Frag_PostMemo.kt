package com.project.churchproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.frag_post_memo.*
import java.text.SimpleDateFormat
import java.util.*

class Frag_PostMemo : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    val simpleDate = SimpleDateFormat("yyyy/MM/dd")     //가져오고싶은 형태 지정

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val rootView = inflater.inflate(R.layout.frag_post_memo, container, false)
        rootView.findViewById<Button>(R.id.Memo_Save_Bttn).setOnClickListener {
            val fragMemo = Frag_MemoList()
            (activity as BasicActivity).replaceFragment(fragMemo)
            SaveMemo()
        }
        return rootView
    }

    fun SaveMemo(){
        auth = Firebase.auth
        val user = auth.currentUser

        val now : Long = System.currentTimeMillis() //현재시간 가져오기
        val mDate = Date(now)   //Date형식으로 바꾸기기

        val getTime: String = simpleDate.format(mDate)
        val memberMemo = MemberMemo(
            user?.uid.toString(),
            Memo_Title_EditText.text.toString(),
            Memo_Inside_EditText.text.toString(),
            getTime
        )


        db.collection("memos").add(memberMemo).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("결과", "메모가 저장되었습니다")

            }
            else{
                Log.e("결과", "메모 저장 실패")
            }
        }
    }
}