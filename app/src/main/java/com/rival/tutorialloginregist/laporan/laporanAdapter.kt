package com.rival.tutorialloginregist.laporan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rival.tutorialloginregist.Pencatatan.lahanModel
import com.rival.tutorialloginregist.R
import java.text.SimpleDateFormat
import java.util.Date

class laporanAdapter(private val context: Context, private val dataList: MutableList<laporanModel>) :
    RecyclerView.Adapter<laporanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val kodeLahanTextView: TextView = itemView.findViewById(R.id.kodeLahanTextView)
        val lokasiLahanTextView: TextView = itemView.findViewById(R.id.lokasiLahanTextView)
        val jenisTextView: TextView = itemView.findViewById(R.id.jenisTextView)
        val jumlahTextView: TextView = itemView.findViewById(R.id.totalTextView)
        val tanggalTextView: TextView = itemView.findViewById(R.id.tanggalTanam)
        val jumlahHariTextView: TextView = itemView.findViewById(R.id.JumlahHari)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_laporan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.kodeLahanTextView.text = data.kode_lahan
        holder.lokasiLahanTextView.text = data.lokasi_lahan
        holder.jenisTextView.text = data.varietas_pohon
        holder.jumlahTextView.text = data.total_bibit
        holder.tanggalTextView.text = data.tanggal

        val tanggalTanam = data.tanggal
        val jumlahHari = hitungSelisihHari(tanggalTanam)
        holder.jumlahHariTextView.text = "$jumlahHari hari"
    }

    fun updateData(newData: List<laporanModel>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }

        private fun hitungSelisihHari(tanggalTanam: String): Long {
            try {
                // Format tanggal yang diambil dari data menjadi objek Date
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val tanggalTanamDate = sdf.parse(tanggalTanam)

                // Objek Date untuk tanggal hari ini
                val tanggalHariIni = Date()

                // Menghitung selisih waktu dalam milisekon
                val selisihWaktu = tanggalHariIni.time - tanggalTanamDate.time

                // Menghitung selisih hari dari selisih waktu
                val selisihHari = selisihWaktu / (24 * 60 * 60 * 1000)

                return selisihHari
            } catch (e: Exception) {
                // Tangani kesalahan jika terjadi
                e.printStackTrace()
            }

            // Kembalikan nilai 0 jika terjadi kesalahan
            return 0
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
