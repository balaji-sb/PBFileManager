package com.pb.filemanager.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.pb.filemanager.utils.Const
import com.pb.filemanager.utils.PackageUtils
import kotlin.reflect.KClass


/**
 * Created by balaji on 3/9/20 9:14 AM
 */


abstract class BaseActivity : AppCompatActivity() {

    private var mContext: Context? = null
    var TAG = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = this
        setContentView(getLayoutResource())
        TAG = getScreenName()
        setupViews()
    }

    //TODO:Need to implement all lifecycle methods


    /**
     * navigate activity for classes whoever extends Appcompatactivity
     */

    fun navigateActivity(
        activity: KClass<out AppCompatActivity>,
        bundle: Bundle = Bundle(),
        isNewActivity: Boolean = false
    ) {
        try {
            val intent = Intent(mContext, activity.java)
            if (isNewActivity)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtras(bundle)
            mContext?.startActivity(intent)
            PackageUtils.hideKeyboard(activity.objectInstance)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * Navigate fragment with backstack
     */

    fun navigateFragmentWithBackStack(fragment: Fragment, bundle: Bundle = Bundle()) {
        mContext?.let {
            val fragmentManager = (it as FragmentActivity).supportFragmentManager
            var transaction = fragmentManager.beginTransaction()
            transaction.addToBackStack(fragment.javaClass.simpleName)
            fragment.arguments = bundle
            transaction =
                transaction.replace(Const.frameId, fragment, fragment.javaClass.simpleName)
            transaction.commit()
        }
    }

    /**
     * Navigate fragment without backstack
     */

    fun navigateFragment(fragment: Fragment, bundle: Bundle = Bundle()) {
        mContext?.let {
            val fragmentManager = (it as FragmentActivity).supportFragmentManager
            var transaction = fragmentManager.beginTransaction()
            fragment.arguments = bundle
            transaction =
                transaction.replace(Const.frameId, fragment, fragment.javaClass.simpleName)
            transaction.commit()
        }
    }

    /**
     * Ask required Permission
     *
     * @param permission
     */
    open fun askPermission(permission: Array<String> = emptyArray()) {
        if (permission.isEmpty()) {
            throw Exception("No permission requested")
        }
        ActivityCompat.requestPermissions(
            (mContext as Activity),
            permission,
            Const.PERMISSION_CODE
        )
    }

    /**
     * Check if required permission is granted or not
     *
     * @param permission
     * @return
     */

    open fun checkPermission(permission: Array<String> = emptyArray()): Boolean {
        if (permission.isEmpty()) {
            throw Exception("No permission requested")
        }
        var isPermissionGranted = false
        for (i in permission.indices) {
            isPermissionGranted = ActivityCompat.checkSelfPermission(
                (mContext as Activity),
                permission[i]
            ) == PackageManager.PERMISSION_GRANTED
        }
        return isPermissionGranted
    }

    override fun onBackPressed() {
        var handled = false
        val availableFragments = supportFragmentManager.fragments
        availableFragments.forEach { fragment ->
            if (fragment is BaseFragment) {
                handled = fragment.onBackPressed()
                if (handled) return
            }
        }

        if (!handled)
            super.onBackPressed()
    }

    abstract fun getLayoutResource(): Int

    abstract fun getScreenName(): String

    abstract fun setupViews()

}