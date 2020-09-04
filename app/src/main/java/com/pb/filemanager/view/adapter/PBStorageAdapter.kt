package com.pb.filemanager.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pb.filemanager.R
import com.pb.filemanager.utils.ProgressBarAnimation
import kotlinx.android.synthetic.main.child_folder_item_2.view.*
import pb.file.manager.fileutils.formatAsFileSize
import pb.file.manager.model.StorageModel


/**
 * Created by balaji on 16/8/20 9:38 AM
 */


class PBStorageAdapter constructor(val listener: ItemClickListener) :
    RecyclerView.Adapter<PBStorageAdapter.StorageViewHolder>() {

    private var storageList = arrayListOf<StorageModel>()

    fun setDirectoryItems(directoryList: ArrayList<StorageModel>) {
        this.storageList = directoryList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.child_folder_item_2, parent, false)
        return StorageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return storageList.size
    }

    override fun onBindViewHolder(holder: StorageViewHolder, position: Int) {
        holder.bindItems(storageList[position])
    }

    interface ItemClickListener {
        fun setOnItemClickListener(model: StorageModel)
    }

    inner class StorageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(model: StorageModel) {
            itemView.storageLabelTxt.text = model.name
            itemView.itemCountTxt.text = "${model.itemCount} Items"
            itemView.totalSpaceTxt.text = "Total Space : ${model.totalSpace.formatAsFileSize}"
            itemView.availableSpaceTxt.text =
                "Available Space : ${model.availableSpace.formatAsFileSize}"
            itemView.usagePercentageTxt.text = "${model.usagePercentage}%"

            itemView.setOnClickListener {
                listener.setOnItemClickListener(model)
            }

            val anim =
                ProgressBarAnimation(itemView.usageProgress, 0f, model.usagePercentage.toFloat())
            anim.duration = 2000
            itemView.usageProgress.startAnimation(anim)
        }
    }

}