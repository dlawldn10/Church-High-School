package com.project.churchproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class Frag_Contents : Fragment() , RecylcerItemClickInterface {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.frag_contents, container, false)
        val db = FirebaseFirestore.getInstance()
        var PhotoPath : EventNoteInfo? = EventNoteInfo("", "")
        val eventNote = rootView.findViewById<ImageView>(R.id.EventNote_ImageView)

        db.collection("event_notes")
            .document("thisWeek").get()
            .addOnSuccessListener { documentSnapshot  -> //데이터베이스에서

                PhotoPath = documentSnapshot.toObject(EventNoteInfo::class.java)
                if(PhotoPath?.path == null){
                    Toast.makeText(
                        context, "주보를 업데이트 해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    Glide.with(this).load(PhotoPath?.path).into(eventNote)
                    Toast.makeText(
                        context, "주보 업데이트",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Log.e("결과", PhotoPath?.path.toString())

            }.addOnFailureListener {exception ->
                Log.d("결과", "get failed with ", exception)
                Toast.makeText(
                    context, "주보를 업데이트 해 주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }

        rootView.findViewById<Button>(R.id.EventNote_Update_Bttn).setOnClickListener {
            val intent = Intent(context, GalleryActivity::class.java)
            intent.putExtra("from", "EventNoteView")
            startActivity(intent)
        }


        return rootView
    }






}