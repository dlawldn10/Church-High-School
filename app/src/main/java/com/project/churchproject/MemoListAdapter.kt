package com.project.churchproject

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.frag_post_memo.*
import java.util.ArrayList


open class MemoListAdapter(private val myMemoList: ArrayList<MemberMemo>?, val activity: Activity?) :
    RecyclerView.Adapter<MemoListAdapter.MemosViewHolder>() {
    var Title : String? = null

    class MemosViewHolder(val MemoLists: LinearLayout) : RecyclerView.ViewHolder(MemoLists)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MemoListAdapter.MemosViewHolder {

        val MemoList = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memo_list, parent, false) as LinearLayout
        val memosViewHolder = MemosViewHolder(MemoList)

        MemoList.setOnClickListener {   //원래 있던 메모 중 하나 선택
//            val pos = memosViewHolder.adapterPosition
//            var memoData : MemberMemo = myMemoList!![pos]
//            val fragPostMemo = Frag_PostMemo()
//            (activity as BasicActivity).replaceFragment(fragPostMemo)
//            var title = fragPostMemo.Memo_Title_EditText.text
//            var inside = fragPostMemo.Memo_Inside_EditText.text

        }

        return memosViewHolder
    }





    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MemosViewHolder, position: Int) {
        var title = holder.MemoLists.findViewById<TextView>(R.id.Memo_Tilte_TextView)
        var date = holder.MemoLists.findViewById<TextView>(R.id.Memo_Date_TextView)

        var memoData : MemberMemo = myMemoList!![position]

//        holder.MemoLists
//            .setOnClickListener {      //누른 메모창으로 넘어가기
////            val intent2 = Intent(context, ChatActivity::class.java)
////            intent2.putExtra("FRIEND_SELECTED", Title)
////            context?.startActivity(intent2)
//
//            Log.e("결과", "터치")
//        }



        date.text = memoData.date
        title.text = memoData.memoTitle
        Log.e("결과", date.text.toString())

        

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myMemoList?.size ?: 0
}
