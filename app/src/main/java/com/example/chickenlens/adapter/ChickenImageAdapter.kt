package com.example.chickenlens.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chickenlens.R

class ChickenImageAdapter(private val listDayChicken: ArrayList<imageData>) : RecyclerView.Adapter<ChickenImageAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chickenImage : ImageView = view.findViewById(R.id.chickenImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ChickenImageAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = listDayChicken.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chicken = listDayChicken[position]
        val chickenImage = chicken.gambar
        Glide.with(holder.itemView)
            .load(chickenImage)
            .into(holder.chickenImage)
    }
}