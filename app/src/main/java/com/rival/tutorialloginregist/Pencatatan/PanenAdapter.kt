package com.rival.tutorialloginregist.Pencatatan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.R

class PanenAdapter(private val context: Context, private val dataList: MutableList<PanenModel>) :
    RecyclerView.Adapter<PanenAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val kodeLahanTextView: TextView = itemView.findViewById(R.id.kodeLahanTextView)
        val lokasiLahanTextView: TextView = itemView.findViewById(R.id.lokasiLahanTextView)
        val jenisTextView: TextView = itemView.findViewById(R.id.jenisTextView)
        val jumlahTextView: TextView = itemView.findViewById(R.id.totalTextView)
        val tanggalTextView: TextView = itemView.findViewById(R.id.tanggalTanam)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_panen, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.kodeLahanTextView.text = data.kode_kopi
        holder.lokasiLahanTextView.text = data.varietas_kopi
        holder.jenisTextView.text = data.metode_pengolahan
        holder.jumlahTextView.text = data.tgl_roasting
        holder.tanggalTextView.text = data.tgl_panen
    }

    fun updateData(newData: List<PanenModel>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
