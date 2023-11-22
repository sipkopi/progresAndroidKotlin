package com.rival.tutorialloginregist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(private val listDomains: ArrayList<ListDomain>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.viewholder2, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.feederName.text = listDomains[position].title


        val drawableResourceId = holder.itemView.context.resources.getIdentifier(listDomains[position].url, "drawable", holder.itemView.context.packageName)

        Glide.with(holder.itemView.context)
            .load(drawableResourceId)
            .into(holder.removeItem)
    }

    override fun getItemCount(): Int {
        return listDomains.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feederName: TextView = itemView.findViewById(R.id.feederName)
        val removeItem: ImageView = itemView.findViewById(R.id.removeFeeder)

    }
}
