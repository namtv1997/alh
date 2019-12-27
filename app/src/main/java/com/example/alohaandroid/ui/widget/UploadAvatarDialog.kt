package com.example.alohaandroid.ui.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.alohaandroid.R
import kotlinx.android.synthetic.main.upload_avatar_dialog.*
import java.util.*

class UploadAvatarDialog(context: Context, private val listener: UploadAvatarDialogListener) :
    Dialog(context), View.OnClickListener {

    interface UploadAvatarDialogListener {
        fun onGetPhotoGallery()

        fun onTakePhotos()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_avatar_dialog)
        Objects.requireNonNull<Window>(window).setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        val window = window
        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        window?.setLayout((size.x * 1), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        tvGetPhotoGallery.setOnClickListener(this)
        tvTakePhotos.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            tvGetPhotoGallery.id -> {
                dismiss()
                listener.onGetPhotoGallery()
            }

            tvTakePhotos.id -> {
                dismiss()
                listener.onTakePhotos()
            }
        }
    }
}