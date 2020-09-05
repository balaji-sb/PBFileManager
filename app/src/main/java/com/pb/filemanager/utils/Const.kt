package com.pb.filemanager.utils

import android.Manifest

/**
 * Created by balaji on 3/9/20 9:24 AM
 */


object Const {
    const val ONE = 1
    const val ZERO = 0
    var frameId: Int = 0
    const val PERMISSION_CODE = 112
    const val SPLASH_INTERVAL = 2000L
    const val FILE = "file"
    const val FILE_PATH = "file_path"

    val reqPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}