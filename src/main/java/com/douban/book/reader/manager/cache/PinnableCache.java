package com.douban.book.reader.manager.cache;

import com.douban.book.reader.manager.exception.DataLoadException;
import com.douban.book.reader.util.Logger;
import java.util.Collection;
import java.util.List;

public class PinnableCache<T extends Identifiable> implements Cache<T>, Pinnable {
    private static final String TAG = PinnableCache.class.getSimpleName();
    private BaseFileCache<T> mFileCache = null;
    private GeneralDbCache<T> mPinnedCache = null;
    private Cache<T> mWrappedCache = null;

    public PinnableCache(Cache<T> wrapped, BaseFileCache<T> fileCache, Class<T> cls) {
        this.mWrappedCache = wrapped;
        this.mPinnedCache = new GeneralDbCache(cls);
        this.mFileCache = fileCache;
    }

    public T get(Object id) throws DataLoadException {
        T entity = this.mWrappedCache.get(id);
        if (entity == null) {
            try {
                entity = this.mPinnedCache.get(id);
            } catch (Throwable th) {
            }
            if (entity == null && this.mFileCache != null) {
                try {
                    entity = this.mFileCache.get(id);
                } catch (Throwable th2) {
                }
            }
            if (entity != null) {
                try {
                    add(entity);
                } catch (Throwable th3) {
                }
            }
        }
        return entity;
    }

    public void add(T entity) throws DataLoadException {
        this.mWrappedCache.add(entity);
        if (this.mFileCache != null) {
            try {
                if (this.mFileCache.has(entity.getId())) {
                    this.mFileCache.add(entity);
                }
            } catch (Throwable th) {
            }
        }
        if (this.mPinnedCache.has(entity.getId())) {
            this.mPinnedCache.add(entity);
        }
    }

    public void addAll(Collection<T> dataList) throws DataLoadException {
        for (T data : dataList) {
            add(data);
        }
    }

    public void sync(Collection<T> collection) throws DataLoadException {
        throw new UnsupportedOperationException();
    }

    public void delete(Object id) throws DataLoadException {
        this.mWrappedCache.delete(id);
        this.mPinnedCache.delete(id);
        if (this.mFileCache != null) {
            try {
                this.mFileCache.delete(id);
            } catch (Throwable e) {
                Logger.ec(TAG, e);
            }
        }
    }

    public void deleteAll() throws DataLoadException {
        this.mWrappedCache.deleteAll();
        this.mPinnedCache.deleteAll();
    }

    public List<T> getAll() throws DataLoadException {
        throw new UnsupportedOperationException();
    }

    public void pin(Object id) throws DataLoadException {
        T entity = get(id);
        if (entity != null) {
            if (this.mFileCache != null) {
                try {
                    this.mFileCache.add(entity);
                } catch (Throwable e) {
                    Logger.ec(TAG, e);
                }
            }
            this.mPinnedCache.add(entity);
        }
    }

    public void unpin(Object id) throws DataLoadException {
        if (this.mFileCache != null) {
            try {
                this.mFileCache.delete(id);
            } catch (Throwable e) {
                Logger.ec(TAG, e);
            }
        }
        this.mPinnedCache.delete(id);
    }
}
