package com.example.shayariapp

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shayariapp.databinding.RvItemBinding

class RvAdapter(var context: Activity, var shayariList: List<DataEntity>):
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    class ViewHolder(val binding: RvItemBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=RvItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shayariList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        var currentItem=shayariList[position]
//       holder.binding.textView.text=currentItem.shayari
    }
}