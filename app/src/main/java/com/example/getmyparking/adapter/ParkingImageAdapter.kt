package com.example.getmyparking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.getmyparking.R
import com.example.getmyparking.interfaces.ParkingImagesListener

class ParkingImageAdapter(
    val urlList : List<String>,
    val listener:ParkingImagesListener
): RecyclerView.Adapter<ParkingImageAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val imgView: ImageView = itemView.findViewById(R.id.img_parking)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(){

            val options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(imgView)
                .load(urlList[adapterPosition])
                .thumbnail(0.33f)
                .apply(options)
                .into(imgView)
        }
        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION)
                listener.onImageClick(adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_parking_image,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return urlList.size
    }

}