package com.project.churchproject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


open class ChatAdapter(private val ChatList: ArrayList<ChatData>?, val myName: String?) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    

    class ChatViewHolder(val ChatBubbleLists: LinearLayout) : RecyclerView.ViewHolder(ChatBubbleLists)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ChatAdapter.ChatViewHolder {

        val ChatBubbles = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_bubble, parent, false) as LinearLayout
        val chatViewHolder = ChatViewHolder(ChatBubbles)

        return chatViewHolder
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var name = holder.ChatBubbleLists.findViewById<TextView>(R.id.TextView_name)
        var msg = holder.ChatBubbleLists.findViewById<TextView>(R.id.TextView_msg)

        var chatData : ChatData = ChatList!![position]

        if(chatData.name.equals(myName)){
            name.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            msg.textAlignment = View.TEXT_ALIGNMENT_TEXT_END

        }else{
            name.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            msg.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        }

        msg.text = chatData.msg
        name.text = chatData.name
        Log.e("결과", msg.text.toString())

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = ChatList?.size ?: 0

    public fun addChat(chatData: ChatData?){
        ChatList?.add(chatData!!)
        notifyItemInserted(getItemCount()-1)    //데이터 갱신.
        Log.e("결과 addChat", ChatList.toString())
    }
}
