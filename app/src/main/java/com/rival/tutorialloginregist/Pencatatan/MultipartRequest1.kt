package com.rival.tutorialloginregist.Pencatatan

import android.content.Context
import android.net.Uri
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException

class MultipartRequest1(
    url: String,
    private val context: Context, // Tambahkan parameter context
    private val params: Map<String, String>,
    private val fileUri: Uri?,
    private val listener: Response.Listener<String>,
    errorListener: Response.ErrorListener
) : Request<String>(
    Method.POST,
    url,
    errorListener
) {

    private val boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW"
    private val twoHyphens = "--"
    private val lineEnd = "\r\n"
    private val PROTOCOL_CHARSET = "utf-8"

    override fun getHeaders(): MutableMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "multipart/form-data; boundary=$boundary"
        return headers
    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val dataOutputStream = java.io.DataOutputStream(byteArrayOutputStream)

        try {
            // Add parameters
            for ((key, value) in params) {
                dataOutputStream.writeBytes("$twoHyphens$boundary$lineEnd")
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"$key\"$lineEnd$lineEnd")
                dataOutputStream.writeBytes("$value$lineEnd")
            }

            // Add image
            dataOutputStream.writeBytes("$twoHyphens$boundary$lineEnd")
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"gambar1\"; filename=\"image.jpg\"$lineEnd")
            dataOutputStream.writeBytes("Content-Type: image/jpeg$lineEnd$lineEnd")

            // Read image file into byte array
            val imageBytes = readBytesFromUri(fileUri)
            dataOutputStream.write(imageBytes)
            dataOutputStream.writeBytes(lineEnd)

            // End
            dataOutputStream.writeBytes("$twoHyphens$boundary$twoHyphens$lineEnd")
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            dataOutputStream.use {
            }
            byteArrayOutputStream.use {
            }
        }

        return byteArrayOutputStream.toByteArray()
    }

    private fun readBytesFromUri(uri: Uri?): ByteArray? {
        uri?.let {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                val byteBuffer = ByteArrayOutputStream()
                val bufferSize = 1024
                val buffer = ByteArray(bufferSize)

                var len: Int
                while (inputStream?.read(buffer).also { len = it!! } != -1) {
                    byteBuffer.write(buffer, 0, len)
                }

                return byteBuffer.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
        return try {
            val jsonString = String(response.data, Charsets.UTF_8)
            Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(VolleyError(e))
        }
    }

    override fun getBodyContentType(): String {
        return "multipart/form-data; boundary=$boundary"
    }

    override fun deliverResponse(response: String) {
        listener.onResponse(response)
    }
}
