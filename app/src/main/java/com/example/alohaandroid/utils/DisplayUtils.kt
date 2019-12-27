package com.example.aloha_call_android.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

object DisplayUtils {

    private const val MAX_IMAGE_MSG_WIDTH = 400

    private var displaySize = Point()
    var imageMessageSize = Point()
    private var displayMetrics = DisplayMetrics()
    private var density = 1f

    var stampItemSize: Int = 0

//    fun fromHtml(html: String): Spanned {
//
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
//        } else {
//            Html.fromHtml(html)
//        }
//    }

    /**
     * Check screen resolution
     *
     * @param context          the application context
     * @param newConfiguration
     */
    fun checkDisplaySize(context: Context, newConfiguration: Configuration?) {
        try {
            density = context.resources.displayMetrics.density
            var configuration: Configuration? = newConfiguration
            if (configuration == null) {
                configuration = context.resources.configuration
            }

            val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = manager.defaultDisplay
            display?.let {
                it.getMetrics(displayMetrics)
                it.getSize(displaySize)
            }

            configuration?.let {
                if (it.screenWidthDp != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
                    val newSize = Math.ceil((it.screenWidthDp * density).toDouble()).toInt()
                    if (Math.abs(displaySize.x - newSize) > 3) {
                        displaySize.x = newSize
                    }
                }
                if (it.screenHeightDp != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
                    val newSize = Math.ceil((it.screenHeightDp * density).toDouble()).toInt()
                    if (Math.abs(displaySize.y - newSize) > 3) {
                        displaySize.y = newSize
                    }
                }
            }

            val width = displaySize.x / 4

            imageMessageSize.x = if (width > MAX_IMAGE_MSG_WIDTH) MAX_IMAGE_MSG_WIDTH else width
            imageMessageSize.y = imageMessageSize.x

        } catch (e: Exception) {
        }
    }

    /**
     * Convert Dip to Pixels base on each screen resolution
     *
     * @param context application context
     * @param dip dip value from dimens.xml
     */
    fun convertDipToPixels(context: Context, dip: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.resources.displayMetrics).toInt()
    }
}