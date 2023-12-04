package com.rival.tutorialloginregist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecAadapter(private val coffeeList: ArrayList<coffe>) : RecyclerView.Adapter<RecAadapter.ViewHolder>(), Filterable {

    private var coffeeListFiltered: ArrayList<coffe> = ArrayList(coffeeList) // Inisialisasi dengan copy dari coffeeList
    private lateinit var context: Context


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgTitle: ImageView = itemView.findViewById(R.id.imgTitle)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvIng: TextView = itemView.findViewById(R.id.tvIng)
        val constraintRow: ConstraintLayout = itemView.findViewById(R.id.constraint_row2)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    private var onItemClickListener: ((coffe) -> Unit)? = null

    fun setOnItemClickListener(listener: (coffe) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("RecAadapter", "onBindViewHolder called for position $position")

        val currentItem = coffeeListFiltered[position]

        Glide.with(holder.imgTitle.context)
            .load(currentItem.gambar1)
            .into(holder.imgTitle)

        holder.tvName.text = currentItem.varietasKopi
        holder.tvIng.text = currentItem.metodePengolahan

        holder.constraintRow.setOnClickListener {
            onItemClickListener?.invoke(currentItem)
        }

        holder.constraintRow.setOnLongClickListener {
            // Handle long click event here
            return@setOnLongClickListener true
        }

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.cardView.context, R.anim.scale_up))
    }

    override fun getItemCount(): Int {
        return coffeeListFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.toLowerCase()

                val filterResults = FilterResults()
                val filteredList = ArrayList<coffe>()

                if (queryString.isNullOrBlank()) {
                    filteredList.addAll(coffeeList)
                } else {
                    for (item in coffeeList) {
                        if (item.varietasKopi.toLowerCase().contains(queryString) ||
                            item.metodePengolahan.toLowerCase().contains(queryString)
                        ) {
                            filteredList.add(item)
                        }
                    }
                }

                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                coffeeListFiltered = results?.values as ArrayList<coffe>
                notifyDataSetChanged()
            }
        }
    }
}
