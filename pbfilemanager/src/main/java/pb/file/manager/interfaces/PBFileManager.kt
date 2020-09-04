package pb.file.manager.interfaces

import pb.file.manager.model.FileModel
import pb.file.manager.model.StorageModel


/**
 * Created by balaji on 3/9/20 7:52 AM
 */


interface PBFileManager {

    fun getAvailableStorage(): ArrayList<StorageModel>

    fun getFilesByPath(filePath: String): ArrayList<FileModel>

}