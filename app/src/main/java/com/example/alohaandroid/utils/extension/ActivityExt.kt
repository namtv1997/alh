package com.example.alohaandroid.utils.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.alohaandroid.R


/**
 * @author hungnt
 * @since 22/1/19.
 */

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment, fragment.javaClass.name)
    }
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}

fun AppCompatActivity.hideKeyboard() {
    val imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content)?.windowToken, 0)
}

fun AppCompatActivity.setupActionBar(toolbar: Toolbar, action: ActionBar.() -> Unit) {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        action()
    }
}

fun AppCompatActivity.showToast(msg: String) {
    android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_SHORT).show()
}

/*
 alertDialog
 */
fun AppCompatActivity.alertDialog( mesage: String) {
    this.let {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setMessage(mesage)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }
        builder.create()

        builder.show()
    }
}




/**
 * Show dialog message with two buttons
 * & set click listener for positive button (dialog will be dismiss after callback called)
 * default click listener for negative button is dismiss dialog
 */
/*fun Activity.showDialogMsg(@StringRes message: Int,
                           @StringRes positiveButton: Int = R.string.button_ok,
                           @StringRes negativeButton: Int = R.string.button_dismiss,
                           onPositiveClickListener: View.OnClickListener? = null) {
    CustomDialog(this,
        getString(message),
        getString(positiveButton),
        getString(negativeButton),
        onPositiveClickListener).show()
}*/

fun AppCompatActivity.findOrCreateViewFragment(@IdRes id: Int, clazz: Class<out Fragment>): Fragment =
    supportFragmentManager.findFragmentById(id) ?: clazz.newInstance()

fun AppCompatActivity.showFragmentToActivity(fragment: Fragment, visible: Boolean) {
    supportFragmentManager.transact {
        if (visible) show(fragment) else hide(fragment)
    }
}