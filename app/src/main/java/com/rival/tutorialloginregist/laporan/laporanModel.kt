package com.rival.tutorialloginregist.laporan

data class laporanModel(
    val kode_lahan: String,
    val lokasi_lahan: String,
    val varietas_pohon: String,
    val total_bibit: String,
    val tanggal:String
) {
    // Properti untuk menampung jumlah hari
    var jumlahHari: Long = 0
}
