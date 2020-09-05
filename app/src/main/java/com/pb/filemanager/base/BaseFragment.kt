package com.pb.filemanager.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.pb.filemanager.utils.Const


/**
 * Created by balaji on 3/9/20 9:14 AM
 */


abstract class BaseFragment : Fragment() {

    var TAG = ""
    var mView: View? = null

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = getScreenName()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(getLayoutResource(), container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mView = view
        initValues()
        setUpViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * Get current fragment
     *
     * @return
     */

    fun getCurrentFragment(): Fragment? {
        val fragmentManager: FragmentManager? = activity?.supportFragmentManager
        return fragmentManager?.findFragmentById(Const.frameId)
    }

    /**
     * Navigate to previous fragment
     */

    fun navigateToPreviousFragment() {
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    /**
     * Navigate fragment with backstack
     */

    fun navigateFragmentWithBackStack(fragment: Fragment, bundle: Bundle = Bundle()) {
        activity?.let {
            if (it is BaseActivity) it.navigateFragmentWithBackStack(fragment, bundle)
        }
    }

    /**
     * Navigate fragment without backstack
     */

    fun navigateFragment(fragment: Fragment, bundle: Bundle = Bundle()) {
        activity?.let {
            if (it is BaseActivity) it.navigateFragment(fragment, bundle)
        }
    }

    /**
     * Check runtime permissions
     */

    fun checkPermission(permission: Array<String> = emptyArray()): Boolean {
        activity?.let {
            if (it is BaseActivity) {
                return it.checkPermission(permission)
            }
        }
        return false
    }

    /**
     * ask runtime permissions
     */

    fun askPermission(permission: Array<String> = emptyArray()) {
        activity?.let {
            if (it is BaseActivity) it.askPermission(permission)
        }
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    abstract fun getLayoutResource(): Int

    abstract fun getScreenName(): String

    abstract fun initValues()

    abstract fun setUpViews()

}