package com.project.churchproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.main.*


class GalleryAdapter(
    private val myDataset: ArrayList<String?>?,
    val galleryActivity: Activity,
    val from: String
) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryAdapter.GalleryViewHolder {

        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters
        val galleryViewHolder = GalleryViewHolder(cardView)

        cardView.setOnClickListener {      //갤러리 화면에서 선택한 이미지를 프로필 사진으로 띄워줌.

            if(from.equals("EventNoteView")){

                (galleryActivity as GalleryActivity).onItemSelected("EVENT_NOTE_PATH",
                    myDataset?.get(galleryViewHolder.adapterPosition).toString())

//                resultIntent.putExtra("EVENT_NOTE_PATH", myDataset?.get(galleryViewHolder.adapterPosition).toString())
//                activity.setResult(201, resultIntent)
                Log.e("결과: 어댑터", myDataset?.get(galleryViewHolder.adapterPosition).toString())


            }else if(from.equals("MemberInintActivity")){
                val resultIntent = Intent()
                resultIntent.putExtra(
                    "PROFILE_PHOTO_PATH",
                    myDataset?.get(galleryViewHolder.adapterPosition).toString()
                )
                galleryActivity.setResult(Activity.RESULT_OK, resultIntent)
            }

            galleryActivity.finish()
        }

        return galleryViewHolder
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        var imageView = holder.cardView.findViewById<ImageView>(R.id.imageView)
        Glide.with(galleryActivity).load(myDataset?.get(position)).centerCrop().override(500).into(
            imageView
        );


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset?.size ?: 0


}
