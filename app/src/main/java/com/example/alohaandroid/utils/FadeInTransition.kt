package com.example.alohaandroid.utils

import android.transition.AutoTransition
import android.transition.Transition

class FadeInTransition private constructor()// force callers to call the factory method to instantiate this class
    : AutoTransition() {
    companion object {

        private val FADE_IN_DURATION = 200

        fun createTransition(): Transition {
            val transition = AutoTransition()
            transition.duration = FADE_IN_DURATION.toLong()
            return transition
        }
    }
}