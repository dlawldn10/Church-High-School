package com.project.churchproject

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


open class FragChatFriendsAdapter(private val myFriendList: ArrayList<String>?, val context: Context?) :
    RecyclerView.Adapter<FragChatFriendsAdapter.FriendsViewHolder>() {
    var friendName : String? = null

    class FriendsViewHolder(val PeopleLists: LinearLayout) : RecyclerView.ViewHolder(PeopleLists)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): FragChatFriendsAdapter.FriendsViewHolder {

        val PeopleLists = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friends, parent, false) as LinearLayout
        val friendsViewHolder = FriendsViewHolder(PeopleLists)


        
        PeopleLists.setOnClickListener {      //누른 사람과의 채팅창으로 이동하기.
            val intent2 = Intent(context, ChatActivity::class.java)
            intent2.putExtra("FRIEND_SELECTED", friendName)
            context?.startActivity(intent2)

            Log.e("결과", "터치")
        }

        return friendsViewHolder
    }





    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        var textView = holder.PeopleLists.findViewById<TextView>(R.id.friendItem_textView)
        textView.text = myFriendList!![position]
        friendName = myFriendList!![position]

        

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myFriendList?.size ?: 0
}
