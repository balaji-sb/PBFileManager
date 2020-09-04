package com.pb.filemanager.base

import android.app.Application
import com.pb.filemanager.R
import com.pb.filemanager.utils.Const

/**
 * Created by balaji on 4/9/20 10:30 PM
 */


class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Const.frameId = R.id.fileFrame
    }

}