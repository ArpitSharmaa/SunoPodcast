package com.example.sunopodcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListofLivePodcastAdapter(val list:List<String>,val listener:(String)->Unit):RecyclerView.Adapter<ListofLivePodcastAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val text = view.findViewById<TextView>(R.id.textView3)
        init {
            itemView.setOnClickListener {
                listener.invoke(text.text.toString())

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.podacstlistitem,parent,false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            text.text = list.get(position)
        }
    }
}