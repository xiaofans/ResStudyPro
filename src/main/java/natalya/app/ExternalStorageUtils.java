package natalya.app;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.IOException;
import natalya.io.FileUtils;
import natalya.log.NLog;

public class ExternalStorageUtils {

    public enum DirType {
        CACHE {
            public String getName() {
                return "cache";
            }
        },
        FILE {
            public String getName() {
                return Keys.file;
            }
        };

        abstract String getName();
    }

    @Deprecated
    public static File getExternalCacheDir(String packageName) {
        File file = null;
        if (!(packageName == null || packageName.trim().equals(""))) {
            File extDir = Environment.getExternalStorageDirectory();
            if (extDir != null) {
                file = new File(extDir.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + packageName + File.separator + "cache");
                try {
                    FileUtils.mkdirs(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    @Deprecated
    public static File getExternalFilesDir(String packageName, String type) {
        File file = null;
        if (!(packageName == null || packageName.trim().equals(""))) {
            File extDir = Environment.getExternalStorageDirectory();
            if (extDir != null) {
                if (type == null) {
                    type = "";
                }
                file = new File(extDir.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + packageName + File.separator + "files" + File.separator + type);
                try {
                    FileUtils.mkdirs(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            appCacheDir = getExternalDir(context, DirType.CACHE);
        }
        if (appCacheDir == null) {
            return context.getCacheDir();
        }
        return appCacheDir;
    }

    public static File getFileDirectory(Context context) {
        File appFileDir = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            appFileDir = getExternalDir(context, DirType.FILE);
        }
        if (appFileDir == null) {
            return context.getFilesDir();
        }
        return appFileDir;
    }

    public static File getCacheDirectory(Context context, String fileDir) {
        File cacheDir = getCacheDirectory(context);
        File file = new File(cacheDir, fileDir);
        if (file.exists() || file.mkdirs()) {
            return file;
        }
        return cacheDir;
    }

    public static File getFileDirectory(Context context, String fileDir) {
        File cacheDir = getFileDirectory(context);
        File file = new File(cacheDir, fileDir);
        if (file.exists() || file.mkdirs()) {
            return file;
        }
        return cacheDir;
    }

    private static File getExternalDir(Context context, DirType dirType) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appDir = new File(new File(dataDir, context.getPackageName()), dirType.getName());
        if (appDir.exists()) {
            return appDir;
        }
        try {
            new File(dataDir, ".nomedia").createNewFile();
        } catch (IOException e) {
            NLog.e("Nataly-Core", "Can't create \".nomedia\" file in application external cache directory");
        }
        if (appDir.mkdirs()) {
            return appDir;
        }
        NLog.w("Nataly-Core", "Unable to create external cache directory");
        return null;
    }
}
