package com.example.getmyparking.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.getmyparking.R
import com.example.getmyparking.interfaces.ParkingImagesListener
import timber.log.Timber

class ParkingImageAdapter(
    val urlList : ArrayList<Drawable>,
    val listener:ParkingImagesListener
): RecyclerView.Adapter<ParkingImageAdapter.ViewHolder>() {

    var isFirstImageFailed = false

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val imgView: ImageView = itemView.findViewById(R.id.img_parking)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(){
            imgView.setImageDrawable(urlList[absoluteAdapterPosition])
        }

        override fun onClick(v: View?) {
            if (absoluteAdapterPosition != RecyclerView.NO_POSITION)
                listener.onImageClick(absoluteAdapterPosition)
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

    fun submitUrls(url:Drawable){
        urlList.add(url)
        notifyItemInserted(urlList.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitUrls(urlList: ArrayList<Drawable>){
        urlList.clear()
        urlList.addAll(urlList)
        notifyDataSetChanged()
    }



}