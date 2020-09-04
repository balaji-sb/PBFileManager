package pb.file.manager.fileutils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.core.os.EnvironmentCompat
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created by balaji on 24/8/20 9:30 PM
 */

class PBDirectories : File {

    private var mUserLabel: String? = null
    private var mState: String? = null
    private var mWriteState: String? = null
    private var mType: String? = null
    private var mUuid: String? = null
    private var mPrimary = false
    private var mRemovable = false
    private var mEmulated = false
    private var mAllowMassStorage = false
    private var mMaxFileSize: Long = 0
    private var mFile: File? = null
    private var mCache: File? = null
    private var userDir: String? = null

    constructor(context: Context) : super(
        Environment.getDataDirectory().absolutePath
    ) {
        if (userDir == null) userDir = "/Android/data/" + context.packageName
        mState = Environment.MEDIA_MOUNTED
        mFile = context.filesDir
        mCache = context.cacheDir
        mType =
            TYPE_INTERNAL
        mWriteState =
            WRITE_APPONLY
    }

    constructor(storage: Any) :
            super(storage.javaClass.getMethod("getPath").invoke(storage) as String) {
        for (method in storage.javaClass.methods) {
            setMethodValues(method, storage)
        }
    }

    @Throws(
        InvocationTargetException::class,
        IllegalAccessException::class
    )
    private fun setMethodValues(
        method: Method,
        storage: Any
    ) {
        if (method.name == "getUserLabel" && method.typeParameters.isEmpty() && method.returnType == String::class.java) mUserLabel =
            method.invoke(storage) as String
        if (method.name == "getUuid" && method.typeParameters.isEmpty() && method.returnType == String::class.java) mUuid =
            method.invoke(storage) as String
        if (method.name == "getState" && method.typeParameters.isEmpty() && method.returnType == String::class.java) mState =
            method.invoke(storage) as String
        if (method.name == "isRemovable" && method.parameterTypes.isEmpty() && method.returnType == Boolean::class.javaPrimitiveType) mRemovable =
            method.invoke(storage) as Boolean
        if (method.name == "isPrimary" && method.parameterTypes.isEmpty() && method.returnType == Boolean::class.javaPrimitiveType) mPrimary =
            method.invoke(storage) as Boolean
        if (method.name == "isEmulated" && method.parameterTypes.isEmpty() && method.returnType == Boolean::class.javaPrimitiveType) mEmulated =
            method.invoke(storage) as Boolean
        if (method.name == "allowMassStorage" && method.parameterTypes.isEmpty() && method.returnType == Boolean::class.javaPrimitiveType) mAllowMassStorage =
            method.invoke(storage) as Boolean
        if (method.name == "getMaxFileSize" && method.parameterTypes.isEmpty() && method.returnType == Long::class.javaPrimitiveType) mMaxFileSize =
            method.invoke(storage) as Long
        if (mState == null) mState = getState()
        mType = if (mPrimary) TYPE_PRIMARY else {
            val path = absolutePath.toLowerCase()
            if (path.indexOf("sd") > 0) TYPE_SD
            else if (path.indexOf("usb") > 0) TYPE_USB
            else "$TYPE_UNKNOWN $absolutePath"
        }
    }

    fun getType(): String? {
        return mType
    }

    @SuppressLint("ObsoleteSdkInt")
    fun getState(): String? {
        if (mRemovable && mState == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) mState =
                Environment.getExternalStorageState(this) else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) mState =
                Environment.getStorageState(this) else if (canRead() && totalSpace > 0) mState =
                Environment.MEDIA_MOUNTED else if (mState == null || Environment.MEDIA_MOUNTED == mState) mState =
                EnvironmentCompat.MEDIA_UNKNOWN
        }
        return mState
    }

    fun getFilesDir(): File {
        if (mFile == null) {
            mFile = File(this, "$userDir/files")
            if (!mFile!!.exists()) mFile!!.mkdirs()
        }
        return mFile!!
    }

    fun getCacheDir(): File {
        if (mCache == null) {
            mCache = File(this, "$userDir/cache")
            if (!mCache!!.exists()) mCache!!.mkdirs()
        }
        return mCache!!
    }

    fun getAccess(): String? {
        if (mWriteState == null) {
            try {
                mWriteState =
                    WRITE_NONE
                val root = listFiles()
                if (root == null || root.size == 0) throw IOException("Root Empty/Unreadable")
                mWriteState =
                    WRITE_READONLY
                var temp = createTempFile("mine", null, getFilesDir())
                temp.delete()
                mWriteState =
                    WRITE_APPONLY
                temp = createTempFile("mine", null, this)
                temp.delete()
                mWriteState =
                    WRITE_FULL
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return mWriteState
    }

    fun isAvailable(): Boolean {
        val s = getState()
        return Environment.MEDIA_MOUNTED == s || Environment.MEDIA_MOUNTED_READ_ONLY == s
    }

    fun isPrimary(): Boolean {
        return mPrimary
    }

    fun isRemovable(): Boolean {
        return mRemovable
    }

    fun isEmulated(): Boolean {
        return mEmulated
    }

    fun isAllowMassStorage(): Boolean {
        return mAllowMassStorage
    }

    fun getMaxFileSize(): Long {
        return mMaxFileSize
    }

    fun getUserLabel(): String? {
        return mUserLabel
    }

    fun getUuid(): String? {
        return mUuid
    }

    companion object {
        private const val TYPE_INTERNAL = "internal"
        private const val TYPE_EXTERNAL = "external"
        private const val TYPE_PRIMARY = "primary"
        private const val TYPE_USB = "USB"
        private const val TYPE_SD = "MicroSD"
        private const val TYPE_UNKNOWN = "unknown"
        private const val WRITE_NONE = "none"
        private const val WRITE_READONLY = "readonly"
        private const val WRITE_APPONLY = "apponly"
        private const val WRITE_FULL = "readwrite"
    }
}
