package com.example.alohaandroid.utils

import android.transition.AutoTransition
import android.transition.Transition

class FadeOutTransition private constructor()// force callers to call the factory method to instantiate this class
    : AutoTransition() {
    companion object {

        private val FADE_OUT_DURATION = 250

        /**
         * Creates a AutoTransition that calls the [android.transition.Transition.TransitionListener.onTransitionEnd]
         * of the passing Listener when complete
         */
        fun withAction(finishingAction: Transition.TransitionListener): Transition {
            val transition = AutoTransition()
            transition.duration = FADE_OUT_DURATION.toLong()
            transition.addListener(finishingAction)
            return transition
        }
    }

}