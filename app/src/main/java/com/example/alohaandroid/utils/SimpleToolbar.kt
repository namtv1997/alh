package com.example.alohaandroid.utils

import android.content.Context
import android.util.AttributeSet
import com.example.alohaandroid.R

class SimpleToolbar(context: Context, attrs: AttributeSet) : TransformingToolbar(context, attrs) {

    init {
        setBackgroundColor(context.resources.getColor(android.R.color.white))
        setNavigationIcon(R.drawable.ic_action_search)
    }

}