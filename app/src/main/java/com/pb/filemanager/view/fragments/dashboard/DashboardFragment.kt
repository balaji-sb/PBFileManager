package com.pb.filemanager.view.fragments.dashboard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.pb.filemanager.R
import com.pb.filemanager.base.BaseFragment
import com.pb.filemanager.utils.Const
import com.pb.filemanager.view.adapter.PBStorageAdapter
import kotlinx.android.synthetic.main.layout_recycler.*
import pb.file.manager.BaseFileManager
import pb.file.manager.interfaces.PBFileManager
import pb.file.manager.model.StorageModel

/**
 * Created by balaji on 3/9/20 9:13 AM
 */


class DashboardFragment : BaseFragment(), PBStorageAdapter.ItemClickListener {

    private lateinit var mPBFileManager: PBFileManager
    private var pbStorageAdapter: PBStorageAdapter? = null
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun getLayoutResource(): Int {
        return R.layout.fragment_dashboard
    }

    override fun getScreenName(): String {
        return this.javaClass.simpleName
    }

    override fun initValues() {
        mPBFileManager = BaseFileManager.getInstance(context)
        pbStorageAdapter = PBStorageAdapter(this)
    }

    override fun setUpViews() {
        genericRecycler?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = pbStorageAdapter
        }
        updateAdapter()
    }

    private fun updateAdapter() {
        pbStorageAdapter?.setDirectoryItems(getAvailableStorage())
    }

    private fun getAvailableStorage(): ArrayList<StorageModel> {
        return mPBFileManager.getAvailableStorage()
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

    override fun setOnItemClickListener(model: StorageModel) {

    }

}