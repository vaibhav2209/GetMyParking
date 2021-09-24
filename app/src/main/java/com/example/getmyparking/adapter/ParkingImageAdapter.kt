package com.example.getmyparking.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
    val urlList : ArrayList<String>,
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
                .placeholder(R.drawable.loading_image_placeholer)
                .error(R.drawable.fail_image_placeholder)
                .fitCenter()

            Glide.with(imgView)
                .load(urlList[absoluteAdapterPosition])
                .thumbnail(0.33f)
                .apply(options)
                .listener(
                    object : RequestListener<Drawable?> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any,
                            target: Target<Drawable?>,
                            isFirstResource: Boolean
                        ): Boolean {
                            imgView.setImageResource(R.drawable.img_parking_place)
                            return true
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    }
                )
                .into(imgView)
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

    fun submitUrls(urls:List<String>){
        urlList.addAll(urls)
        notifyItemRangeInserted(0, urls.size)
    }

}