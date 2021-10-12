package com.example.getmyparking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.getmyparking.R
import com.example.getmyparking.data.local.CarEntity
import com.example.getmyparking.interfaces.UserVehicleAdapterListener
import com.example.getmyparking.models.Vehicle

class UserVehiclesAdapter(
    private val vehicleList:ArrayList<CarEntity>,
    private val listener: UserVehicleAdapterListener
): RecyclerView.Adapter<UserVehiclesAdapter.ViewHolder>() {

    private val differCallback = object: DiffUtil.ItemCallback<CarEntity>(){
        override fun areItemsTheSame(oldItem: CarEntity, newItem: CarEntity): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: CarEntity, newItem: CarEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list:List<CarEntity>) = differ.submitList(list)


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val txtVehicleModel: TextView = itemView.findViewById(R.id.txt_vehical_model)
        private val txtVehicleNumber: TextView = itemView.findViewById(R.id.txt_vehicle_number)
        private val txtVehicleType: TextView = itemView.findViewById(R.id.txt_vehicle_type)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(){

            txtVehicleNumber.text = differ.currentList[absoluteAdapterPosition].number
            txtVehicleModel.text = differ.currentList[absoluteAdapterPosition].model
            txtVehicleType.text = differ.currentList[absoluteAdapterPosition].type.name

        }
        override fun onClick(v: View?) {
            if (absoluteAdapterPosition != RecyclerView.NO_POSITION)
                listener.onVehicleClick(differ.currentList[absoluteAdapterPosition])
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.vehicle_detail_layout,
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