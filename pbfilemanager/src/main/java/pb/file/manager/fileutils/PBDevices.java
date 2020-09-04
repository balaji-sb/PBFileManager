package pb.file.manager.fileutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.core.os.EnvironmentCompat;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by balaji on 12/8/20 7:52 PM
 */


public class PBDevices extends File {

    private final static String TYPE_INTERNAL = "internal";
    private final static String TYPE_EXTERNAL = "external";
    private final static String TYPE_PRIMARY = "primary";
    private final static String TYPE_USB = "USB";
    private final static String TYPE_SD = "MicroSD";
    private final static String TYPE_UNKNOWN = "unknown";

    private final static String WRITE_NONE = "none";
    private final static String WRITE_READONLY = "readonly";
    private final static String WRITE_APPONLY = "apponly";
    private final static String WRITE_FULL = "readwrite";


    private String mUserLabel, mState, mWriteState, mType, mUuid;
    private boolean mPrimary, mRemovable, mEmulated, mAllowMassStorage;
    private long mMaxFileSize;
    private File mFile, mCache;
    private String userDir;

    public PBDevices(Context context) {
        super(Environment.getDataDirectory().getAbsolutePath());
        if (userDir == null) userDir = "/Android/data/" + context.getPackageName();
        mState = Environment.MEDIA_MOUNTED;
        mFile = context.getFilesDir();
        mCache = context.getCacheDir();
        mType = TYPE_INTERNAL;
        mWriteState = WRITE_APPONLY;
    }

    public PBDevices(Object storage) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        super((String) storage.getClass().getMethod("getPath", null).invoke(storage, null));
        for (Method method : storage.getClass().getMethods()) {
            setMethodValues(method, storage);
        }
    }

    private void setMethodValues(Method method, Object storage) throws InvocationTargetException, IllegalAccessException {
        if (method.getName().equals("getUserLabel") && method.getTypeParameters().length == 0 && method.getReturnType().equals(String.class))
            mUserLabel = (String) method.invoke(storage, null);
        if (method.getName().equals("getUuid") && method.getTypeParameters().length == 0 && method.getReturnType().equals(String.class))
            mUuid = (String) method.invoke(storage, null);
        if (method.getName().equals("getState") && method.getTypeParameters().length == 0 && method.getReturnType().equals(String.class))
            mState = (String) method.invoke(storage, null);
        if (method.getName().equals("isRemovable") && method.getParameterTypes().length == 0 && method.getReturnType() == boolean.class)
            mRemovable = (Boolean) method.invoke(storage, null);
        if (method.getName().equals("isPrimary") && method.getParameterTypes().length == 0 && method.getReturnType() == boolean.class)
            mPrimary = (Boolean) method.invoke(storage, null);
        if (method.getName().equals("isEmulated") && method.getParameterTypes().length == 0 && method.getReturnType() == boolean.class)
            mEmulated = (Boolean) method.invoke(storage, null);
        if (method.getName().equals("allowMassStorage") && method.getParameterTypes().length == 0 && method.getReturnType() == boolean.class)
            mAllowMassStorage = (Boolean) method.invoke(storage, null);
        if (method.getName().equals("getMaxFileSize") && method.getParameterTypes().length == 0 && method.getReturnType() == long.class)
            mMaxFileSize = (Long) method.invoke(storage, null);

        if (mState == null)
            mState = getState();

        if (mPrimary)
            mType = TYPE_PRIMARY;
        else {
            String path = getAbsolutePath().toLowerCase();
            if (path.indexOf("sd") > 0)
                mType = TYPE_SD;
            else if (path.indexOf("usb") > 0)
                mType = TYPE_USB;
            else
                mType = TYPE_UNKNOWN + " " + getAbsolutePath();
        }
    }

    public String getType() {
        return mType;
    }

    @SuppressLint("ObsoleteSdkInt")
    public String getState() {
        if (mRemovable && mState == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                mState = Environment.getExternalStorageState(this);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                mState = Environment.getStorageState(this);
            else if (canRead() && getTotalSpace() > 0)
                mState = Environment.MEDIA_MOUNTED;
            else if (mState == null || Environment.MEDIA_MOUNTED.equals(mState))
                mState = EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return mState;
    }

    public File getFilesDir() {
        if (mFile == null) {
            mFile = new File(this, userDir + "/files");
            if (!mFile.exists())
                mFile.mkdirs();
        }
        return mFile;
    }

    public File getCacheDir() {
        if (mCache == null) {
            mCache = new File(this, userDir + "/cache");
            if (!mCache.exists())
                mCache.mkdirs();
        }
        return mCache;
    }

    public String getAccess() {
        if (mWriteState == null) {
            try {
                mWriteState = WRITE_NONE;
                File[] root = listFiles();
                if (root == null || root.length == 0)
                    throw new IOException("Root Empty/Unreadable");
                mWriteState = WRITE_READONLY;
                File temp = File.createTempFile("mine", null, getFilesDir());
                //noinspection ResultOfMethodCallIgnored
                temp.delete();
                mWriteState = WRITE_APPONLY;
                temp = File.createTempFile("mine", null, this);
                //noinspection ResultOfMethodCallIgnored
                temp.delete();
                mWriteState = WRITE_FULL;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mWriteState;
    }

    public boolean isAvailable() {
        String s = getState();
        return Environment.MEDIA_MOUNTED.equals(s) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(s);
    }

    public boolean isPrimary() {
        return mPrimary;
    }

    public boolean isRemovable() {
        return mRemovable;
    }

    public boolean isEmulated() {
        return mEmulated;
    }

    public boolean isAllowMassStorage() {
        return mAllowMassStorage;
    }

    public long getMaxFileSize() {
        return mMaxFileSize;
    }

    public String getUserLabel() {
        return mUserLabel;
    }

    public String getUuid() {
        return mUuid;
    }

}
