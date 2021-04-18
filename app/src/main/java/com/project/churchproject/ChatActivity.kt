package com.project.churchproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var msgList : ArrayList<ChatData>? = null
    private val myName : String? = "임지우"

    val database = Firebase.database
    val myRef = database.getReference("messages")
    val chat : ChatData = ChatData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        Send_Bttn.setOnClickListener {
            var msg : String = msg_EditText.text.toString()

            if(msg != null){
                chat.msg = msg
                chat.name = myName
                myRef.push().setValue(chat) //푸시를 해야 데이터가 묶음으로 들어감
            }

        }

        msgList = ArrayList()   //하 이게 문제였네..밑에서 null이라 안나오는거였음. 해결!
        viewManager = LinearLayoutManager(this)
        viewAdapter = ChatAdapter(msgList, myName)
        recyclerView = findViewById<RecyclerView>(R.id.msg_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // Write a message to the database


        val childEventListener = object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                var chat : ChatData? = dataSnapshot.getValue<ChatData>()
                (viewAdapter as ChatAdapter).addChat(chat)
                Log.e("결과", chat.toString())

                // A new comment has been added, add it to the displayed list
//                val comment = dataSnapshot.getValue<Comment>()

                // ...
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
//                val newComment = dataSnapshot.getValue<Comment>()
//                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
//                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
//                val movedComment = dataSnapshot.getValue<Comment>()
//                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        myRef.addChildEventListener(childEventListener)



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    val intent = intent
                    val myName = intent.getStringExtra("CURRENTUSER_NAME")
                    val friendName = intent.getStringExtra("FRIEND_SELECTED")   //여기서부터 다시. 선택한 친구의 이름을 받았고...
                    Log.e("인텐트", myName.toString())
                }
            }
        }
    }
}

