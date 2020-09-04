package pb.file.manager

import android.content.Context
import pb.file.manager.fileutils.PBFileManagerImpl

/**
 * Created by balaji on 4/9/20 11:00 PM
 */


object BaseFileManager {

    fun getInstance(context: Context?): PBFileManagerImpl {
        return PBFileManagerImpl(context = context)
    }

}