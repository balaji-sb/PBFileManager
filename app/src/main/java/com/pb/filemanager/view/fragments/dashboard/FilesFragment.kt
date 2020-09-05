package com.pb.filemanager.view.fragments.dashboard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.recyclerview.widget.LinearLayoutManager
import com.pb.filemanager.R
import com.pb.filemanager.base.BaseFragment
import com.pb.filemanager.utils.Const
import com.pb.filemanager.view.adapter.PBFileAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.layout_recycler.*
import pb.file.manager.BaseFileManager
import pb.file.manager.interfaces.PBFileManager
import pb.file.manager.model.FileModel
import java.io.File

/**
 * Created by balaji on 3/9/20 9:13 AM
 */


class FilesFragment : BaseFragment(), PBFileAdapter.ItemClickListener {

    private lateinit var mPBFileManager: PBFileManager
    private var pBFileAdapter: PBFileAdapter? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    private var parentPath = ""
    private var currentPath: String? = null

    override fun getLayoutResource(): Int {
        return R.layout.fragment_dashboard
    }

    override fun getScreenName(): String {
        return this.javaClass.simpleName
    }

    override fun initValues() {
        mPBFileManager = BaseFileManager.getInstance(context)
        pBFileAdapter = PBFileAdapter(this)
    }

    override fun setUpViews() {
        genericRecycler?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = pBFileAdapter
        }
        if (checkPermission(Const.reqPermissions)) {
            arguments?.apply {
                getString(Const.FILE_PATH, null)?.let { path ->
                    parentPath = path
                    updatePathTitle()
                    updateAdapter()
                }
            }
        } else askPermission()
    }

    private fun updateAdapter() {
        pBFileAdapter?.setFileAdapterItems(retrieveDirectories())
    }

    private fun retrieveDirectories(): ArrayList<FileModel> {
        return mPBFileManager.getFilesByPath(currentPath ?: parentPath)
    }

    private fun storageScanIntentFilter(): IntentFilter {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL)
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED)
        filter.addAction(Intent.ACTION_MEDIA_REMOVED)
        filter.addAction(Intent.ACTION_MEDIA_SHARED)
        filter.addDataScheme(Const.FILE)
        return filter
    }

    private fun startReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                updateAdapter()
            }
        }
        context?.registerReceiver(broadcastReceiver, storageScanIntentFilter())
    }

    private fun stopReceiver() {
        broadcastReceiver?.let {
            context?.unregisterReceiver(it)
        }
    }

    override fun onResume() {
        super.onResume()
        startReceiver()
    }

    override fun onPause() {
        super.onPause()
        stopReceiver()
    }

    override fun setOnItemClickListener(model: FileModel) {
        if (model.isDirectory) {
            currentPath = model.path
            updatePathTitle()
            updateAdapter()
        }
    }

    override fun onBackPressed(): Boolean {
        return if (parentPath == currentPath) false
        else {
            val currentFile = File(currentPath!!)
            currentPath = currentFile.parent
            updatePathTitle()
            updateAdapter()
            true
        }
    }

    private fun updatePathTitle() {
        var formattedFilePath = ""
        if (currentPath == null) {
            formattedFilePath = parentPath.replace(parentPath, "Home >")
        } else {
            currentPath?.apply {
                formattedFilePath = if (this == parentPath) {
                    replace(parentPath, "Home >")
                } else {
                    replace(parentPath, "Home").replace("/", " > ")
                }
            }
        }
        pathTitleTxt.text = formattedFilePath
    }

}