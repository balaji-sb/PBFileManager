package com.pb.filemanager.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.pb.filemanager.utils.Const.ONE
import com.pb.filemanager.utils.Const.ZERO

/**
 * Created by balaji on 3/9/20 9:19 AM
 */


object PackageUtils {

    /**
     * Show soft keyboard
     */
    fun showKeyboard(activity: Activity?) {
        activity?.let {
            val view: View? = it.currentFocus
            if (null != view && null != view.windowToken && EditText::class.java.isAssignableFrom(
                    view.javaClass
                )
            ) {
                val inputMethodManager =
                    it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, ONE)
            } else {
                it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }


    }

    /**
     * Hide soft keyboard
     */

    fun hideKeyboard(activity: Activity?) {
        activity?.let {
            val inputMethodManager =
                it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = it.currentFocus
            if (null != view && null != view.windowToken && EditText::class.java.isAssignableFrom(
                    view.javaClass
                )
            ) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, ZERO)
            } else {
                it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            }
        }
    }

}