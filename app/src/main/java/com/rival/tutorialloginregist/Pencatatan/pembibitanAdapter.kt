package com.rival.tutorialloginregist.Pencatatan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.R

class pembibitanAdapter(private val context: Context, private val dataList: MutableList<lahanModel>) :
    RecyclerView.Adapter<pembibitanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val kodeLahanTextView: TextView = itemView.findViewById(R.id.kodeLahanTextView)
        val lokasiLahanTextView: TextView = itemView.findViewById(R.id.lokasiLahanTextView)
        val jenisTextView: TextView = itemView.findViewById(R.id.jenisTextView)
        val jumlahTextView: TextView = itemView.findViewById(R.id.totalTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.kodeLahanTextView.text = data.kode_lahan
        holder.lokasiLahanTextView.text = data.lokasi_lahan
        holder.jenisTextView.text = data.varietas_pohon
        holder.jumlahTextView.text = data.total_bibit
    }

    fun updateData(newData: List<lahanModel>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return dataList.size
    }
}
