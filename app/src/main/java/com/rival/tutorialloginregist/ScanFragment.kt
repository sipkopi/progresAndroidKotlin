package com.rival.tutorialloginregist

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.rival.tutorialloginregist.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ScanFragment : Fragment() {
    private lateinit var editText: EditText
    private lateinit var imageView: ImageView
    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Izin diberikan, lanjutkan menyimpan gambar
                saveImageFromImageView()
            } else {
                showToast("Izin ditolak. Tidak dapat menyimpan gambar.")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scan, container, false)
        val btnScan = view.findViewById<Button>(R.id.btn_scann)
        val btnQr = view.findViewById<Button>(R.id.btn_qr)
        editText = view.findViewById(R.id.edt_qrlink)
        imageView = view.findViewById(R.id.imageQR)

        btnScan.setOnClickListener {
            // Panggil fungsi untuk menyimpan gambar
            checkStoragePermission()
        }

        btnQr.setOnClickListener {
            generateQRCode()
        }

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        return view
    }

    private fun saveImageFromImageView() {
        val bitmap = getBitmapFromImageView(imageView)

        // Panggil fungsi untuk menyimpan gambar
        saveImageToStorage(bitmap)
    }

    private fun checkStoragePermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Izin sudah diberikan, lanjutkan menyimpan gambar
                saveImageFromImageView()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                // Izin belum diberikan, minta izin dengan menggunakan ActivityResultLauncher
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            else -> {
                showToast("Versi Android tidak mendukung izin penyimpanan.")
            }
        }
    }

    // Fungsi untuk menyimpan gambar ke penyimpanan lokal
    private fun saveImageToStorage(bitmap: Bitmap) {
        // Periksa apakah izin WRITE_EXTERNAL_STORAGE sudah diberikan
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val externalStoragePublicDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val timestamp = System.currentTimeMillis()
            val fileName = "image_$timestamp.jpg"
            val file = File(externalStoragePublicDirectory, fileName)

            try {
                val fileOutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
                showToast("Gambar disimpan di ${file.absolutePath}")
            } catch (e: IOException) {
                e.printStackTrace()
                showToast("Gagal menyimpan gambar.")
            }
        } else {
            showToast("Izin tidak diberikan. Tidak dapat menyimpan gambar.")
        }
    }

    // Fungsi untuk mendapatkan bitmap dari ImageView
    private fun getBitmapFromImageView(imageView: ImageView): Bitmap {
        val drawable = imageView.drawable
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    // Fungsi untuk menampilkan pesan toast
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null && result.contents != null) {
            Toast.makeText(requireContext(), "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
        }
    }

    // Menambahkan metode untuk menghasilkan QR code
    private fun generateQRCode() {
        val text = editText.text.toString()

        if (text.isNotEmpty()) {
            try {
                val bitMatrix: BitMatrix = MultiFormatWriter().encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    500,
                    500
                )

                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(
                            x,
                            y,
                            if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
                        )
                    }
                }

                imageView.setImageBitmap(bmp)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
    }
}
