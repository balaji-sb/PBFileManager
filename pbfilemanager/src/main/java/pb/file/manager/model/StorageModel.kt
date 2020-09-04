package pb.file.manager.model

/**
 * Created by balaji on 3/9/20 8:03 AM
 */


data class StorageModel(
    val name: String = "",
    val itemCount: Int = 0,
    val path: String = "",
    val usagePercentage: Int = 0,
    val totalSpace: Long = 0L,
    val availableSpace: Long = 0L
)