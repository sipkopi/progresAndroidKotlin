package com.rival.tutorialloginregist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecAadapter(private val coffeeList: ArrayList<coffe>) :
    RecyclerView.Adapter<RecAadapter.ViewHolder>() {

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
        val context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = coffeeList[position]

        Glide.with(holder.imgTitle.context)
            .load(currentItem.gambar1)
            .into(holder.imgTitle)

        holder.tvName.text = currentItem.varietasKopi
        holder.tvIng.text = currentItem.metodePengolahan

        holder.constraintRow.setOnClickListener {
            onItemClickListener?.invoke(currentItem)
        }

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.cardView.context, R.anim.scale_up))
    }

    override fun getItemCount(): Int {
        return coffeeList.size
    }
}


