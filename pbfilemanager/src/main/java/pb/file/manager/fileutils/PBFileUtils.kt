package pb.file.manager.fileutils

import pb.file.manager.R
import java.io.File
import java.lang.Exception
import kotlin.math.log2
import kotlin.math.pow

/**
 * Created by balaji on 3/9/20 7:49 AM
 */


val File.getFileType: Int
    get() = name.let {
        if (isHidden && isDirectory) return R.drawable.ic_twotone_folder_hidden_24
        if (isDirectory) return R.drawable.ic_twotone_folder_open_24
        when (extension) {
            "jpg", "png" -> return R.drawable.ic_twotone_image_24
            else -> return R.drawable.ic_pen_jarvis
        }
    }


val File.formatSize: String
    get() = length().formatAsFileSize

val Int.formatAsFileSize: String
    get() = toLong().formatAsFileSize

val Long.formatAsFileSize: String
    get() = log2(toDouble()).toInt().div(10).let {
        try {
            val precision = when (it) {
                0 -> 0; 1 -> 1; else -> 2
            }
            val prefix = arrayOf("", "K", "M", "G", "T", "P", "E", "Z", "Y")
            return String.format("%.${precision}f ${prefix[it]}B", toDouble() / 2.0.pow(it * 10.0))
        } catch (ex: Exception) {
            ex.printStackTrace()
            return "0 KB"
        }
    }