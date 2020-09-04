package pb.file.manager.model

/**
 * Created by balaji on 16/8/20 9:41 AM
 */


data class FileModel(
    val name: String = "",
    val itemCount: Int = 0,
    val path: String = "",
    val parent: String = "",
    val fileType: Int = 0,
    val fileSize: String = "",
    val isDirectory: Boolean = false,
    val isFile: Boolean = false,
    val usagePercentage: Int = 0,
    val totalSpace: Long = 0L,
    val availableSpace: Long = 0L,
    val isHidden : Boolean =false
)