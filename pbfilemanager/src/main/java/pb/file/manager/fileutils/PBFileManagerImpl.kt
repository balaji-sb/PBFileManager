package pb.file.manager.fileutils

import android.content.Context
import android.os.storage.StorageManager
import pb.file.manager.interfaces.PBFileManager
import pb.file.manager.model.FileModel
import pb.file.manager.model.StorageModel
import java.io.File


/**
 * Created by balaji on 15/8/20 5:23 PM
 */


class PBFileManagerImpl(private val context: Context?) :
    PBFileManager {

    /**
     * To retrieve all the available storage volumes with details by calling this getAvailableStorage() method
     */

    override fun getAvailableStorage(): ArrayList<StorageModel> {
        var storageDevicesList = arrayListOf<StorageModel>()
        val storageVolumes = getStorageVolumes()
        storageVolumes.listIterator().forEach { devices ->
            storageDevicesList = parseStorage(devices)
        }
        return storageDevicesList
    }

    /**
     * To get all the available storage volumes by calling this getStorageVolumes() method using reflections
     */

    private fun getStorageVolumes(): List<PBDevices> {
        val storageVolumes = mutableListOf<PBDevices>()
        val manager = context?.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        try {
            val method = manager.javaClass.getMethod("getVolumeList")
            val volumes = method.invoke(manager) as Array<Any>
            volumes.iterator().forEach {
                storageVolumes.add(
                    PBDevices(it)
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return storageVolumes
    }

    /**
     * To parse the data from storage volumes to custom file model
     */

    private fun parseStorage(devices: PBDevices): ArrayList<StorageModel> {
        val storageDevicesList = arrayListOf<StorageModel>()
        if (devices.getState() != null) {
            val userLabel = if (devices.isPrimary()) "Internal Storage" else devices.getUserLabel()
            if (devices.totalSpace >= 100) {
                val usageSpace = (devices.totalSpace - devices.freeSpace)
                val usagePercentage = (usageSpace * 100) / devices.totalSpace
                val model = StorageModel(
                    name = userLabel!!,
                    path = devices.absolutePath,
                    itemCount = devices.listFiles()?.size ?: 0,
                    totalSpace = if (devices.totalSpace <= 0) 100 else devices.totalSpace,
                    availableSpace = if (devices.freeSpace <= 0) 100 else devices.freeSpace,
                    usagePercentage = usagePercentage.toInt()
                )
                storageDevicesList.add(model)
            }
        }
        return storageDevicesList
    }


    /**
     * to get the list of files from the filepath
     */

    override fun getFilesByPath(filePath: String): ArrayList<FileModel> {
        val filesList = arrayListOf<FileModel>()
        val f = File(filePath)
        f.listFiles()?.iterator()?.forEach { file ->
            val model = FileModel(
                name = file.name,
                path = file.absolutePath,
                parent = file.parent,
                fileSize = file.formatSize,
                fileType = file.getFileType,
                isHidden = file.isHidden,
                isFile = file.isFile,
                itemCount = file?.listFiles()?.size ?: 0,
                isDirectory = file.isDirectory
            )
            filesList.add(model)
        }
        return filesList
    }

}