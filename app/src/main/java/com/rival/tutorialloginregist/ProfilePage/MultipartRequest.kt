package com.rival.tutorialloginregist.ProfilePage

import android.graphics.Bitmap
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException

class MultipartRequest(
    url: String,
    private val params: Map<String, String>,
    private val file: File,
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
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"gambar\"; filename=\"image.jpg\"$lineEnd")
            dataOutputStream.writeBytes("Content-Type: image/jpeg$lineEnd$lineEnd")

            // Read image file into byte array
            val imageBytes = file.readBytes()
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
