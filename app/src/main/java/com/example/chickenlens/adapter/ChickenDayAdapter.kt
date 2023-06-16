package com.example.chickenlens.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chickenlens.R

class ChickenDayAdapter(private val listDayChicken: ArrayList<Chicken>) :
    RecyclerView.Adapter<ChickenDayAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val day: TextView = view.findViewById(R.id.hari_teks)
        val date: TextView = view.findViewById(R.id.tanggal_teks)
        val chickenAvg: TextView = view.findViewById(R.id.rata_ayam)
        val foodAvg: TextView = view.findViewById(R.id.rata_pakan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.chicken_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listDayChicken.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chicken= listDayChicken[position]
        val day = chicken.harike
        val date = chicken.tanggal
        val chickenAvg = chicken.rerataAyam
        val foodAvg = chicken.rerataPakan

        holder.day.text = "Hari Ke : ${day.toString()}"
        holder.date.text = "Tanggal : $date"
        holder.chickenAvg.text = "Rata-Rata Ayam : $chickenAvg Ekor"
        holder.foodAvg.text = "Rata-Rata Pakan : $foodAvg Kg"
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listDayChicken[holder.adapterPosition])
        }
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: Chicken)
    }
}