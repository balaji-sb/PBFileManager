package com.pb.filemanager.view.activities.dashboard

import com.pb.filemanager.R
import com.pb.filemanager.base.BaseActivity
import com.pb.filemanager.view.fragments.dashboard.DashboardFragment

/**
 * Created by balaji on 4/9/20 10:16 PM
 */


class DashboardActivity : BaseActivity() {

    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun getScreenName(): String {
        return this.javaClass.simpleName
    }

    override fun setupViews() {
        navigateFragment(DashboardFragment())
    }

}