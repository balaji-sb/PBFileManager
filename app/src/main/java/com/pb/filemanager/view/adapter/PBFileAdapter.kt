package com.pb.filemanager.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pb.filemanager.R
import kotlinx.android.synthetic.main.child_folder_item.view.*
import pb.file.manager.model.FileModel


/**
 * Created by balaji on 16/8/20 9:38 AM
 */


class PBFileAdapter(val listener: ItemClickListener) :
    RecyclerView.Adapter<PBFileAdapter.PBFileViewHolder>() {

    private var mDirectoryList = arrayListOf<FileModel>()

    fun setFileAdapterItems(directoryList: ArrayList<FileModel>) {
        this.mDirectoryList = directoryList
        notifyDataSetChanged()
    }

    inner class PBFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(model: FileModel) {
            itemView.fileNameTxt.text = model.name
            if (model.isFile)
                itemView.fileItemsTxt.text = model.fileSize
            else
                itemView.fileItemsTxt.text = "${model.itemCount} Items"


            val image = model.fileType
            itemView.fileTypeImg.setImageResource(image)
            itemView.setOnClickListener {
                listener.setOnItemClickListener(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PBFileViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.child_folder_item, parent, false)
        return PBFileViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDirectoryList.size
    }

    override fun onBindViewHolder(holder: PBFileViewHolder, position: Int) {
        holder.bindItems(mDirectoryList[position])
    }

    interface ItemClickListener {
        fun setOnItemClickListener(model: FileModel)
    }
}