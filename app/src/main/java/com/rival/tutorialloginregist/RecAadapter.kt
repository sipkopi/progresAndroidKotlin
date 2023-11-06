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
import com.rival.tutorialloginregist.coffe

class RecAadapter(private val coffeList: ArrayList<coffe>) : RecyclerView.Adapter<RecAadapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgTitle: ImageView = itemView.findViewById(R.id.imgTitle)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvIng: TextView = itemView.findViewById(R.id.tvIng)
        val constraint_row: ConstraintLayout = itemView.findViewById(R.id.constraint_row)
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
        val currentItem = coffeList[position]

        holder.imgTitle.setImageResource(currentItem.imageTitle)
        holder.tvName.text = currentItem.name
        holder.tvIng.text = currentItem.ingredients

        val cont = holder.constraint_row.context
        holder.constraint_row.setOnClickListener {
            onItemClickListener?.invoke(currentItem)
        }

        holder.constraint_row.setOnLongClickListener(View.OnLongClickListener {
            // Handle long click event here
            return@OnLongClickListener true
        })

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.cardView.context, R.anim.scale_up))
    }

    override fun getItemCount(): Int {
        return coffeList.size
    }
}
