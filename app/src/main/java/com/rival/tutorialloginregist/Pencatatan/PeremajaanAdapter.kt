package com.rival.tutorialloginregist.Pencatatan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.R

class PeremajaanAdapter(private val context: Context, private val dataList: MutableList<PeremajaanModel>) :
    RecyclerView.Adapter<PeremajaanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val kodeLahanTextView: TextView = itemView.findViewById(R.id.kodeLahanTextView)
        val lokasiLahanTextView: TextView = itemView.findViewById(R.id.lokasiLahanTextView)
        val jenisTextView: TextView = itemView.findViewById(R.id.jenisTextView)
        val jumlahTextView: TextView = itemView.findViewById(R.id.totalTextView)
        val tanggalTextView : TextView = itemView.findViewById(R.id.tanggalTanam)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_peremajaan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.kodeLahanTextView.text = data.perlakuan
        holder.lokasiLahanTextView.text = data.kode_lahan
        holder.jenisTextView.text = data.tanggal
        holder.jumlahTextView.text = data.pupuk
        holder.tanggalTextView.text = "${data.kebutuhan} ${getSuffix(data.perlakuan)}"
    }

    private fun getSuffix(perlakuan: String): String {
        return when (perlakuan.toLowerCase()) {
            "pemupukan" -> "kg"
            "penyiraman" -> "lt"
            else -> ""
        }
    }

    fun updateData(newData: List<PeremajaanModel>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return dataList.size
    }
}
