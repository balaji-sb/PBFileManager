package com.pb.filemanager.view.activities.splash

import android.Manifest
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import com.pb.filemanager.R
import com.pb.filemanager.base.BaseActivity
import com.pb.filemanager.utils.Const
import com.pb.filemanager.view.activities.dashboard.DashboardActivity

/**
 * Created by balaji on 4/9/20 11:22 PM
 */


class SplashActivity : BaseActivity() {

    private val handler by lazy { Handler(Looper.myLooper()!!) }

    override fun getLayoutResource(): Int {
        return R.layout.activity_splash
    }

    override fun getScreenName(): String {
        return this.javaClass.simpleName
    }

    override fun setupViews() {
        handler.postDelayed(runnable, Const.SPLASH_INTERVAL)
    }

    private val runnable = Runnable {
        if (checkPermission(Const.reqPermissions)) {
            navigateActivity(DashboardActivity::class, isNewActivity = true)
        } else askPermission(Const.reqPermissions)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            var isGrant = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    isGrant = true
                }
            }
            if (isGrant) {
                navigateActivity(DashboardActivity::class, isNewActivity = true)
            } else askPermission(Const.reqPermissions)
        }
    }

}