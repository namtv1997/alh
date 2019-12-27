package com.example.alohaandroid.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileUtils1 {

    /**create image file in local storage**/
    @SuppressLint("SimpleDateFormat")
    fun createImageFile(context: Context): File {
        val timeStamp= SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    @Throws(IOException::class)
    @SuppressLint("SimpleDateFormat")
    fun getOutputMediaFile(): File? {
        val mediaStoreDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "spaceshare")
        if (!mediaStoreDir.exists()) {
            if (!mediaStoreDir.mkdirs()) {
                return null
            }
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File(mediaStoreDir.absolutePath + File.separator + "IMG_"+ timeStamp + ".jpg")
    }

    fun convertBitmapToUri(context: Context, bitmapImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        return Uri.parse(MediaStore.Images.Media.insertImage(context.contentResolver, bitmapImage, "Title", null))
    }
}