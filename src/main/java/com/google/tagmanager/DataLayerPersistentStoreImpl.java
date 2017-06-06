package com.google.tagmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.DataLayer.PersistentStore.Callback;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class DataLayerPersistentStoreImpl implements PersistentStore {
    private static final String CREATE_MAPS_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' STRING NOT NULL, '%s' BLOB NOT NULL, '%s' INTEGER NOT NULL);", new Object[]{MAPS_TABLE, ID_FIELD, KEY_FIELD, "value", "expires"});
    private static final String DATABASE_NAME = "google_tagmanager.db";
    private static final String EXPIRE_FIELD = "expires";
    private static final String ID_FIELD = "ID";
    private static final String KEY_FIELD = "key";
    private static final String MAPS_TABLE = "datalayer";
    private static final int MAX_NUM_STORED_ITEMS = 2000;
    private static final String VALUE_FIELD = "value";
    private Clock mClock;
    private final Context mContext;
    private DatabaseHelper mDbHelper;
    private final Executor mExecutor;
    private int mMaxNumStoredItems;

    @VisibleForTesting
    class DatabaseHelper extends SQLiteOpenHelper {
        private boolean tablePresent(java.lang.String r12, android.database.sqlite.SQLiteDatabase r13) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0025 in list [B:5:0x0022]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r11 = this;
            r10 = 0;
            r8 = 0;
            r1 = "SQLITE_MASTER";	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r0 = 1;	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r2 = new java.lang.String[r0];	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r0 = 0;	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r3 = "name";	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r2[r0] = r3;	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r3 = "name=?";	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r0 = 1;	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r4 = new java.lang.String[r0];	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r0 = 0;	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r4[r0] = r12;	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r5 = 0;	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r6 = 0;	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r7 = 0;	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r0 = r13;	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r0 = r8.moveToFirst();	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            if (r8 == 0) goto L_0x0025;
        L_0x0022:
            r8.close();
        L_0x0025:
            return r0;
        L_0x0026:
            r9 = move-exception;
            r0 = new java.lang.StringBuilder;	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r0.<init>();	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r1 = "Error querying for table ";	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r0 = r0.append(r1);	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r0 = r0.append(r12);	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            r0 = r0.toString();	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            com.google.tagmanager.Log.w(r0);	 Catch:{ SQLiteException -> 0x0026, all -> 0x0044 }
            if (r8 == 0) goto L_0x0042;
        L_0x003f:
            r8.close();
        L_0x0042:
            r0 = r10;
            goto L_0x0025;
        L_0x0044:
            r0 = move-exception;
            if (r8 == 0) goto L_0x004a;
        L_0x0047:
            r8.close();
        L_0x004a:
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.tagmanager.DataLayerPersistentStoreImpl.DatabaseHelper.tablePresent(java.lang.String, android.database.sqlite.SQLiteDatabase):boolean");
        }

        DatabaseHelper(Context context, String databaseName) {
            super(context, databaseName, null, 1);
        }

        public SQLiteDatabase getWritableDatabase() {
            SQLiteDatabase db = null;
            try {
                db = super.getWritableDatabase();
            } catch (SQLiteException e) {
                DataLayerPersistentStoreImpl.this.mContext.getDatabasePath(DataLayerPersistentStoreImpl.DATABASE_NAME).delete();
            }
            if (db == null) {
                return super.getWritableDatabase();
            }
            return db;
        }

        public void onOpen(SQLiteDatabase db) {
            if (VERSION.SDK_INT < 15) {
                Cursor cursor = db.rawQuery("PRAGMA journal_mode=memory", null);
                try {
                    cursor.moveToFirst();
                } finally {
                    cursor.close();
                }
            }
            if (tablePresent(DataLayerPersistentStoreImpl.MAPS_TABLE, db)) {
                validateColumnsPresent(db);
            } else {
                db.execSQL(DataLayerPersistentStoreImpl.CREATE_MAPS_TABLE);
            }
        }

        private void validateColumnsPresent(SQLiteDatabase db) {
            Cursor c = db.rawQuery("SELECT * FROM datalayer WHERE 0", null);
            Set<String> columns = new HashSet();
            try {
                String[] columnNames = c.getColumnNames();
                for (Object add : columnNames) {
                    columns.add(add);
                }
                if (!columns.remove(DataLayerPersistentStoreImpl.KEY_FIELD) || !columns.remove("value") || !columns.remove(DataLayerPersistentStoreImpl.ID_FIELD) || !columns.remove("expires")) {
                    throw new SQLiteException("Database column missing");
                } else if (!columns.isEmpty()) {
                    throw new SQLiteException("Database has extra columns");
                }
            } finally {
                c.close();
            }
        }

        public void onCreate(SQLiteDatabase db) {
            FutureApis.setOwnerOnlyReadWrite(db.getPath());
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private static class KeyAndSerialized {
        final String mKey;
        final byte[] mSerialized;

        KeyAndSerialized(String key, byte[] serialized) {
            this.mKey = key;
            this.mSerialized = serialized;
        }

        public String toString() {
            return "KeyAndSerialized: key = " + this.mKey + " serialized hash = " + Arrays.hashCode(this.mSerialized);
        }
    }

    public DataLayerPersistentStoreImpl(Context context) {
        this(context, new Clock() {
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        }, DATABASE_NAME, MAX_NUM_STORED_ITEMS, Executors.newSingleThreadExecutor());
    }

    @VisibleForTesting
    DataLayerPersistentStoreImpl(Context context, Clock clock, String databaseName, int maxNumStoredItems, Executor executor) {
        this.mContext = context;
        this.mClock = clock;
        this.mMaxNumStoredItems = maxNumStoredItems;
        this.mExecutor = executor;
        this.mDbHelper = new DatabaseHelper(this.mContext, databaseName);
    }

    public void saveKeyValues(List<KeyValue> keysAndValues, final long lifetimeInMillis) {
        final List<KeyAndSerialized> serializedKeysAndValues = serializeValues(keysAndValues);
        this.mExecutor.execute(new Runnable() {
            public void run() {
                DataLayerPersistentStoreImpl.this.saveSingleThreaded(serializedKeysAndValues, lifetimeInMillis);
            }
        });
    }

    public void loadSaved(final Callback callback) {
        this.mExecutor.execute(new Runnable() {
            public void run() {
                callback.onKeyValuesLoaded(DataLayerPersistentStoreImpl.this.loadSingleThreaded());
            }
        });
    }

    public void clearKeysWithPrefix(final String keyPrefix) {
        this.mExecutor.execute(new Runnable() {
            public void run() {
                DataLayerPersistentStoreImpl.this.clearKeysWithPrefixSingleThreaded(keyPrefix);
            }
        });
    }

    private List<KeyValue> loadSingleThreaded() {
        try {
            deleteEntriesOlderThan(this.mClock.currentTimeMillis());
            List<KeyValue> unserializeValues = unserializeValues(loadSerialized());
            return unserializeValues;
        } finally {
            closeDatabaseConnection();
        }
    }

    private List<KeyValue> unserializeValues(List<KeyAndSerialized> serialized) {
        List<KeyValue> result = new ArrayList();
        for (KeyAndSerialized keyAndSerialized : serialized) {
            result.add(new KeyValue(keyAndSerialized.mKey, unserialize(keyAndSerialized.mSerialized)));
        }
        return result;
    }

    private List<KeyAndSerialized> serializeValues(List<KeyValue> keysAndValues) {
        List<KeyAndSerialized> result = new ArrayList();
        for (KeyValue keyAndValue : keysAndValues) {
            result.add(new KeyAndSerialized(keyAndValue.mKey, serialize(keyAndValue.mValue)));
        }
        return result;
    }

    private Object unserialize(byte[] bytes) {
        Throwable th;
        Object obj = null;
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInput = null;
        try {
            ObjectInputStream objectInput2 = new ObjectInputStream(byteStream);
            try {
                obj = objectInput2.readObject();
                if (objectInput2 != null) {
                    try {
                        objectInput2.close();
                    } catch (IOException e) {
                    }
                }
                byteStream.close();
                objectInput = objectInput2;
            } catch (IOException e2) {
                objectInput = objectInput2;
                if (objectInput != null) {
                    try {
                        objectInput.close();
                    } catch (IOException e3) {
                    }
                }
                byteStream.close();
                return obj;
            } catch (ClassNotFoundException e4) {
                objectInput = objectInput2;
                if (objectInput != null) {
                    try {
                        objectInput.close();
                    } catch (IOException e5) {
                    }
                }
                byteStream.close();
                return obj;
            } catch (Throwable th2) {
                th = th2;
                objectInput = objectInput2;
                if (objectInput != null) {
                    try {
                        objectInput.close();
                    } catch (IOException e6) {
                        throw th;
                    }
                }
                byteStream.close();
                throw th;
            }
        } catch (IOException e7) {
            if (objectInput != null) {
                objectInput.close();
            }
            byteStream.close();
            return obj;
        } catch (ClassNotFoundException e8) {
            if (objectInput != null) {
                objectInput.close();
            }
            byteStream.close();
            return obj;
        } catch (Throwable th3) {
            th = th3;
            if (objectInput != null) {
                objectInput.close();
            }
            byteStream.close();
            throw th;
        }
        return obj;
    }

    private byte[] serialize(Object o) {
        byte[] toByteArray;
        Throwable th;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = null;
        try {
            ObjectOutputStream objectOutput2 = new ObjectOutputStream(byteStream);
            try {
                objectOutput2.writeObject(o);
                toByteArray = byteStream.toByteArray();
                if (objectOutput2 != null) {
                    try {
                        objectOutput2.close();
                    } catch (IOException e) {
                    }
                }
                byteStream.close();
                objectOutput = objectOutput2;
            } catch (IOException e2) {
                objectOutput = objectOutput2;
                toByteArray = null;
                if (objectOutput != null) {
                    try {
                        objectOutput.close();
                    } catch (IOException e3) {
                    }
                }
                byteStream.close();
                return toByteArray;
            } catch (Throwable th2) {
                th = th2;
                objectOutput = objectOutput2;
                if (objectOutput != null) {
                    try {
                        objectOutput.close();
                    } catch (IOException e4) {
                        throw th;
                    }
                }
                byteStream.close();
                throw th;
            }
        } catch (IOException e5) {
            toByteArray = null;
            if (objectOutput != null) {
                objectOutput.close();
            }
            byteStream.close();
            return toByteArray;
        } catch (Throwable th3) {
            th = th3;
            if (objectOutput != null) {
                objectOutput.close();
            }
            byteStream.close();
            throw th;
        }
        return toByteArray;
    }

    private synchronized void saveSingleThreaded(List<KeyAndSerialized> keysAndValues, long lifetimeInMillis) {
        try {
            long now = this.mClock.currentTimeMillis();
            deleteEntriesOlderThan(now);
            makeRoomForEntries(keysAndValues.size());
            writeEntriesToDatabase(keysAndValues, now + lifetimeInMillis);
            closeDatabaseConnection();
        } catch (Throwable th) {
            closeDatabaseConnection();
        }
    }

    private List<KeyAndSerialized> loadSerialized() {
        SQLiteDatabase db = getWritableDatabase("Error opening database for loadSerialized.");
        List<KeyAndSerialized> list = new ArrayList();
        if (db != null) {
            Cursor results = db.query(MAPS_TABLE, new String[]{KEY_FIELD, "value"}, null, null, null, null, ID_FIELD, null);
            while (results.moveToNext()) {
                try {
                    list.add(new KeyAndSerialized(results.getString(0), results.getBlob(1)));
                } finally {
                    results.close();
                }
            }
        }
        return list;
    }

    private void writeEntriesToDatabase(List<KeyAndSerialized> keysAndValues, long expireTime) {
        SQLiteDatabase db = getWritableDatabase("Error opening database for writeEntryToDatabase.");
        if (db != null) {
            for (KeyAndSerialized keyAndValue : keysAndValues) {
                ContentValues values = new ContentValues();
                values.put("expires", Long.valueOf(expireTime));
                values.put(KEY_FIELD, keyAndValue.mKey);
                values.put("value", keyAndValue.mSerialized);
                db.insert(MAPS_TABLE, null, values);
            }
        }
    }

    private void makeRoomForEntries(int count) {
        int entrysOverLimit = (getNumStoredEntries() - this.mMaxNumStoredItems) + count;
        if (entrysOverLimit > 0) {
            List<String> entrysToDelete = peekEntryIds(entrysOverLimit);
            Log.i("DataLayer store full, deleting " + entrysToDelete.size() + " entries to make room.");
            deleteEntries((String[]) entrysToDelete.toArray(new String[0]));
        }
    }

    private void clearKeysWithPrefixSingleThreaded(String keyPrefix) {
        SQLiteDatabase db = getWritableDatabase("Error opening database for clearKeysWithPrefix.");
        if (db != null) {
            try {
                Log.v("Cleared " + db.delete(MAPS_TABLE, "key = ? OR key LIKE ?", new String[]{keyPrefix, keyPrefix + ".%"}) + " items");
            } catch (SQLiteException e) {
                Log.w("Error deleting entries with key prefix: " + keyPrefix + " (" + e + ").");
            } finally {
                closeDatabaseConnection();
            }
        }
    }

    private void deleteEntriesOlderThan(long timeInMillis) {
        SQLiteDatabase db = getWritableDatabase("Error opening database for deleteOlderThan.");
        if (db != null) {
            try {
                Log.v("Deleted " + db.delete(MAPS_TABLE, "expires <= ?", new String[]{Long.toString(timeInMillis)}) + " expired items");
            } catch (SQLiteException e) {
                Log.w("Error deleting old entries.");
            }
        }
    }

    private void deleteEntries(String[] entryIds) {
        if (entryIds != null && entryIds.length != 0) {
            SQLiteDatabase db = getWritableDatabase("Error opening database for deleteEntries.");
            if (db != null) {
                try {
                    db.delete(MAPS_TABLE, String.format("%s in (%s)", new Object[]{ID_FIELD, TextUtils.join(",", Collections.nCopies(entryIds.length, "?"))}), entryIds);
                } catch (SQLiteException e) {
                    Log.w("Error deleting entries " + Arrays.toString(entryIds));
                }
            }
        }
    }

    private List<String> peekEntryIds(int maxEntries) {
        List<String> entryIds = new ArrayList();
        if (maxEntries <= 0) {
            Log.w("Invalid maxEntries specified. Skipping.");
        } else {
            SQLiteDatabase db = getWritableDatabase("Error opening database for peekEntryIds.");
            if (db != null) {
                Cursor cursor = null;
                try {
                    cursor = db.query(MAPS_TABLE, new String[]{ID_FIELD}, null, null, null, null, String.format("%s ASC", new Object[]{ID_FIELD}), Integer.toString(maxEntries));
                    if (cursor.moveToFirst()) {
                        do {
                            entryIds.add(String.valueOf(cursor.getLong(0)));
                        } while (cursor.moveToNext());
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (SQLiteException e) {
                    Log.w("Error in peekEntries fetching entryIds: " + e.getMessage());
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Throwable th) {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }
        return entryIds;
    }

    private int getNumStoredEntries() {
        int numStoredEntries = 0;
        SQLiteDatabase db = getWritableDatabase("Error opening database for getNumStoredEntries.");
        if (db == null) {
            return 0;
        }
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) from datalayer", null);
            if (cursor.moveToFirst()) {
                numStoredEntries = (int) cursor.getLong(0);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLiteException e) {
            Log.w("Error getting numStoredEntries");
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return numStoredEntries;
    }

    private SQLiteDatabase getWritableDatabase(String errorMessage) {
        try {
            return this.mDbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            Log.w(errorMessage);
            return null;
        }
    }

    private void closeDatabaseConnection() {
        try {
            this.mDbHelper.close();
        } catch (SQLiteException e) {
        }
    }
}
