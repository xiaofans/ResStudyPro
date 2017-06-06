package com.douban.amonsul.core;

import android.content.Context;
import com.douban.amonsul.MobileStat;
import com.douban.amonsul.StatPrefs;
import com.douban.amonsul.store.AppEventStatHandler;
import com.douban.amonsul.utils.FileUtils;
import java.io.StringWriter;

public class StatAccess {
    private static String IMPORT_EVENT_FILE_NAME = "access_import_file";
    private static String SP_KEY_CRASH_LOCAL_RECORD = "access_crash_local_record";
    private static String SP_KEY_CRASH_UPLOAD = "access_crash_upload_record";
    private static String SP_KEY_EVENT_FILE_UPLOAD = "access_event_file_upload";
    private static String SP_KEY_EVENT_LOCAL_FILE = "access_local_file_number";
    private static String SP_KEY_EVENT_LOCAL_RECORD = "access_event_local_record";
    private static String SP_KEY_EVENT_UPLOAD = "access_event_upload_record";
    private static String SP_KEY_REAL_TIME_EVENT = "access_real_time_event";
    private static String TAG = StatAccess.class.getName();
    private static Boolean WORK = Boolean.valueOf(false);
    private static StatAccess mInstance;
    private Context mContext;
    private final Object mLock = new Object();
    private StatPrefs mPrefs;

    private StatAccess(Context context) {
        this.mContext = context;
        this.mPrefs = StatPrefs.getInstance(context);
    }

    public static synchronized StatAccess getInstance(Context context) {
        StatAccess statAccess;
        synchronized (StatAccess.class) {
            if (mInstance == null) {
                mInstance = new StatAccess(context);
            }
            statAccess = mInstance;
        }
        return statAccess;
    }

    public void setWork(boolean work) {
        WORK = Boolean.valueOf(work);
    }

    public synchronized void evtRecord() {
        if (WORK.booleanValue()) {
            this.mPrefs.putInt(SP_KEY_EVENT_LOCAL_RECORD, this.mPrefs.getInt(SP_KEY_EVENT_LOCAL_RECORD, 0) + 1);
        }
    }

    public synchronized void realTimeEvtRecord() {
        if (WORK.booleanValue()) {
            this.mPrefs.putInt(SP_KEY_REAL_TIME_EVENT, this.mPrefs.getInt(SP_KEY_REAL_TIME_EVENT, 0) + 1);
        }
    }

    public int getRealTimeEvtRecord() {
        return this.mPrefs.getInt(SP_KEY_REAL_TIME_EVENT, 0);
    }

    public void evtUpload(int number) {
        if (WORK.booleanValue()) {
            this.mPrefs.putInt(SP_KEY_EVENT_UPLOAD, this.mPrefs.getInt(SP_KEY_EVENT_UPLOAD, 0) + number);
        }
    }

    public int getEvtRecordCnt() {
        return this.mPrefs.getInt(SP_KEY_EVENT_LOCAL_RECORD, 0);
    }

    public int getEvtUploadCnt() {
        return this.mPrefs.getInt(SP_KEY_EVENT_UPLOAD, 0);
    }

    public void crashRecord() {
        if (WORK.booleanValue()) {
            this.mPrefs.putInt(SP_KEY_CRASH_LOCAL_RECORD, this.mPrefs.getInt(SP_KEY_CRASH_LOCAL_RECORD, 0) + 1);
        }
    }

    public void crashUpload(int number) {
        if (WORK.booleanValue()) {
            this.mPrefs.putInt(SP_KEY_CRASH_UPLOAD, this.mPrefs.getInt(SP_KEY_CRASH_UPLOAD, 0) + number);
        }
    }

    public void importEvtRecord(String tag, String msg) {
        if (WORK.booleanValue()) {
            String content = "[" + tag + "]:" + msg + "\n";
            synchronized (this.mLock) {
                FileUtils.saveDataToLocalFile(this.mContext, IMPORT_EVENT_FILE_NAME, content, true);
            }
        }
    }

    public String getImportEvtRecord() {
        byte[] data = FileUtils.getLocalFileData(this.mContext, IMPORT_EVENT_FILE_NAME);
        try {
            StringWriter writer = new StringWriter();
            writer.write(new String(data, "UTF-8"));
            return writer.toString();
        } catch (Exception e) {
            if (MobileStat.DEBUG) {
                e.printStackTrace();
            }
            return "";
        }
    }

    public int getCrashRecordCnt() {
        return this.mPrefs.getInt(SP_KEY_CRASH_LOCAL_RECORD, 0);
    }

    public int getCrashUpload() {
        return this.mPrefs.getInt(SP_KEY_CRASH_UPLOAD, 0);
    }

    public int getCurrentEventCnt() {
        return MobileStatManager.getInstance(this.mContext).getAppEventHandler().getEventsCnt();
    }

    public int getCurrentFileCnt() {
        return ((AppEventStatHandler) MobileStatManager.getInstance(this.mContext).getAppEventHandler()).getFileDataKeeper().getFileCnt(this.mContext);
    }

    public int getCurrentCrashCnt() {
        return MobileStatManager.getInstance(this.mContext).getCrashEventHandler().getEventsCnt();
    }

    public void evtFileRecord() {
        if (WORK.booleanValue()) {
            this.mPrefs.putInt(SP_KEY_EVENT_LOCAL_FILE, this.mPrefs.getInt(SP_KEY_EVENT_LOCAL_FILE, 0) + 1);
        }
    }

    public int getEvtFileCnt() {
        return this.mPrefs.getInt(SP_KEY_EVENT_LOCAL_FILE, 0);
    }

    public void evtFileUpload() {
        if (WORK.booleanValue()) {
            this.mPrefs.putInt(SP_KEY_EVENT_FILE_UPLOAD, this.mPrefs.getInt(SP_KEY_EVENT_FILE_UPLOAD, 0) + 1);
        }
    }

    public int getEvtFileUploadCnt() {
        return this.mPrefs.getInt(SP_KEY_EVENT_FILE_UPLOAD, 0);
    }

    public void cleanAllData() {
        if (WORK.booleanValue()) {
            FileUtils.removeFile(this.mContext, IMPORT_EVENT_FILE_NAME, false);
            this.mPrefs.putInt(SP_KEY_EVENT_LOCAL_RECORD, 0);
            this.mPrefs.putInt(SP_KEY_EVENT_UPLOAD, 0);
            this.mPrefs.putInt(SP_KEY_CRASH_UPLOAD, 0);
            this.mPrefs.putInt(SP_KEY_CRASH_LOCAL_RECORD, 0);
            this.mPrefs.putInt(SP_KEY_EVENT_FILE_UPLOAD, 0);
            this.mPrefs.putInt(SP_KEY_EVENT_LOCAL_RECORD, 0);
        }
    }
}
