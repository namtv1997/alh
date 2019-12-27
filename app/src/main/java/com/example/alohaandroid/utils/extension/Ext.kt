package com.example.alohaandroid.utils.extension

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.alohaandroid.R
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

fun Fragment.showToast(@StringRes resId: Int) {
    Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(msg: String) {
    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
}

/**
 * Check if given string is a valid email address
 */
fun String.isEmail(): Boolean {
    return if (isNullOrEmpty()) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(this).find()
    }
}

/**
 * Check if given string is a valid real user name
 */
fun String.isUserName(): Boolean {
    return if (isNullOrEmpty()) {
        false
    } else {
        //Pattern.compile("^{1,30}$").matcher(this).matches()
        true
    }
}

/**
 * Check if given string is a valid phone number
 */
fun String.isPhoneNumber(): Boolean {
    return if (isNullOrEmpty()) {
        false
    } else {
        Pattern.compile("^[0-9]{6,15}$").matcher(this).matches()
    }
}

/**
 * Check if given string is a valid password
 * This string should longer than 8 characters and contain at least one Upper case character
 */
fun String.isPassword(): Boolean {
    return if (isNullOrEmpty()) {
        false
    } else {
        Pattern.compile("^(?=.*[A-Z])[a-zA-Z0-9\\s]{8,30}\$").matcher(this).matches()
    }
}


/*fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this, AppFactory(this.context?.applicationContext as App)).get(viewModelClass)*/

fun Fragment.hideKeyboard() {
    activity?.apply {
        currentFocus?.let {
            val imm =
                application.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}

/**
 * Extension function that helps to use lambda expression for retrofit disposable using higher-order
 * function
 */


/**
 * Set on onclick listener for view in order to avoid rapid multiple clicks on the same view
 */
fun View.setOnClickAction(listener: View.OnClickListener) {
    RxView.clicks(this)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe { listener.onClick(this) }
}

fun View.setOnClickAction(action: (view: View) -> Unit) {
    RxView.clicks(this)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe { action(this) }
}

fun <T> Observable<T>.observeOnMain(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.observeOnMain(): Flowable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

/**
 * Converts an intent into a [Bundle] suitable for use as fragment arguments.
 */
fun intentToFragmentArguments(intent: Intent?): Bundle {
    val arguments = Bundle()
    if (intent == null) {
        return arguments
    }

    intent.data?.let {
        arguments.putParcelable("_uri", it)
    }

    intent.extras?.let {
        arguments.putAll(intent.extras)
    }

    return arguments
}

fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.action()
    editor.apply()
}

fun Fragment.replaceFragmentInFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.transact {
        replace(frameId, fragment, fragment.javaClass.name)
    }
}

fun Fragment.switchFragment(fragment: Fragment, replace: Int) {
    val name: String = fragment.javaClass.name
    val ft: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
    ft.replace(replace, fragment)
    ft.addToBackStack(name)
    ft.commit()
}

fun AppCompatActivity.switchFragment(fragment: Fragment, replace: Int) {
    val name: String = fragment.javaClass.name
    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
    ft.replace(replace, fragment)
    ft.addToBackStack(name)
    ft.commit()
}

/*
 alertDialog
 */
fun Fragment.alertDialog(mesage: String) {
    activity.let {
        val builder = it?.let { it1 -> AlertDialog.Builder(it1, R.style.AlertDialogTheme) }
        builder?.setMessage(mesage)

        builder?.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }
        builder?.create()
        builder?.show()

    }

}

fun dpToPx(context: Context, dp: Int): Int {
    val density: Float = context.resources.displayMetrics.density
    return Math.round(dp * density)
}

fun getCurrentTopFragment(fm: FragmentManager): Fragment? {
    val stackCount: Int = fm.backStackEntryCount
    if (stackCount > 0) {
        val backEntry: FragmentManager.BackStackEntry = fm.getBackStackEntryAt(stackCount - 1)
        return fm.findFragmentByTag(backEntry.name)
    } else {
        val fragments: List<Fragment> = fm.fragments
        if (fragments.isNotEmpty()) {
            for (f: Fragment in fragments) {
                if (!f.isHidden) {
                    return f
                }
            }
        }
    }

    return null
}


