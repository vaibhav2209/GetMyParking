package com.example.getmyparking.adapter

import android.location.Address
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.getmyparking.R
import com.example.getmyparking.interfaces.SearchLocationAdapterListener

class SearchLocationAdapter(
    private val listener:SearchLocationAdapterListener
):RecyclerView.Adapter<SearchLocationAdapter.ViewHolder>() {


    private val differCallback = object: DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.featureName == newItem.featureName
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list:List<Address>) = differ.submitList(list)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val txtCity = itemView.findViewById<TextView>(R.id.city_name)
        val txtCountry = itemView.findViewById<TextView>(R.id.country_name)
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(){
            txtCity.text = differ.currentList[adapterPosition].featureName
            txtCountry.text = differ.currentList[adapterPosition].countryName
        }
        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION)
                listener.onSearchResultClick(differ.currentList[adapterPosition])
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_recycler_layout,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}