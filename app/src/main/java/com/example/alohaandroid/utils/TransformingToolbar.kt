package com.example.alohaandroid.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar

open class TransformingToolbar(context: Context, attrs: AttributeSet) : Toolbar(context, attrs) {

    /**
     * Sets the Visibility of all children to GONE
     */
    fun hideContent() {
        for (i in 0 until getChildCount()) {
            getChildAt(i).setVisibility(GONE)
        }
    }

    /**
     * Sets the Visibility of all children to VISIBLE
     */
    fun showContent() {
        for (i in 0 until getChildCount()) {
            getChildAt(i).setVisibility(VISIBLE)
        }
    }

}