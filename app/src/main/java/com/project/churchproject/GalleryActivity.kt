package com.project.churchproject

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_update_member_init.*
import kotlinx.android.synthetic.main.frag_contents.*
import kotlinx.android.synthetic.main.main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class GalleryActivity : BasicActivity() , RecylcerItemClickInterface{
    var requestedView : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        var recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        lateinit var viewAdapter: RecyclerView.Adapter<*>
        lateinit var viewManager: RecyclerView.LayoutManager
        val numberOfColumns : Int = 3



        viewManager = LinearLayoutManager(this)
        val myInterface : RecylcerItemClickInterface?

        viewAdapter = GalleryAdapter(
            getImagesPath(this),
            this,
            intent.getStringExtra("from").toString())


        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {

            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter

            recyclerView.layoutManager = GridLayoutManager(this@GalleryActivity, numberOfColumns)
        }


    }

    private fun getImagesPath(activity: Activity) : ArrayList<String?>{
        val uri: Uri
        val listOfAllImages = ArrayList<String?>()
        val cursor: Cursor?
        val column_index_data: Int
        val column_index_folder_name: Int
        var PathOfImage: String? = null

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        cursor = activity.contentResolver.query(
            uri, projection, null,
            null, null
        )

        column_index_data = cursor!!.getColumnIndexOrThrow(MediaColumns.DATA)
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(PathOfImage)
        }
        return listOfAllImages
    }


    override fun onItemSelected(key : String, value: String) {

        val db = FirebaseFirestore.getInstance()
        val now : Long = System.currentTimeMillis() //현재시간 가져오기
        val mDate = Date(now)   //Date형식으로 바꾸기기
        val simpleDate = SimpleDateFormat("yyyy/MM/dd")     //가져오고싶은 형태 지정
        val getTime: String = simpleDate.format(mDate)

        //나머지 업데이트
        val EventNoteInfo = EventNoteInfo(getTime, value)

        db.collection("event_notes").document("thisWeek").set(EventNoteInfo).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("결과", "User profile updated.")
                Toast.makeText(
                    baseContext, "업데이트가 완료 되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()

            }
            else{
                Log.d("결과", "User profile updated.")
                Toast.makeText(
                    baseContext, "업데이트 실패",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        Log.e("결과 : 컨텐츠", value.toString() )
    }

}