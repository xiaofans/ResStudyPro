package com.douban.book.reader.manager.cache;

import com.douban.book.reader.manager.exception.DataLoadException;
import com.douban.book.reader.util.IOUtils;
import com.douban.book.reader.util.JsonUtils;
import com.douban.book.reader.util.Logger;
import java.io.File;
import java.util.Collection;
import java.util.List;

public abstract class BaseFileCache<T extends Identifiable> implements Cache<T> {
    private static final String TAG = BaseFileCache.class.getSimpleName();
    private Class<T> mType;

    protected abstract File getFile(Object obj);

    public BaseFileCache(Class<T> cls) {
        this.mType = cls;
    }

    public T get(Object id) throws DataLoadException {
        try {
            return (Identifiable) JsonUtils.fromFile(getFile(id), this.mType);
        } catch (Throwable e) {
            throw new DataLoadException(e);
        }
    }

    public void add(T entity) throws DataLoadException {
        if (entity != null) {
            try {
                IOUtils.writeStringToFile(getFile(entity.getId()), JsonUtils.toJson(entity));
            } catch (Throwable e) {
                throw new DataLoadException(e);
            }
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
        try {
            getFile(id).delete();
        } catch (Exception e) {
            Logger.ec(TAG, e);
        }
    }

    public void deleteAll() throws DataLoadException {
        throw new UnsupportedOperationException();
    }

    public List<T> getAll() throws DataLoadException {
        throw new UnsupportedOperationException();
    }

    public boolean has(Object id) {
        File file = getFile(id);
        return file.isFile() && file.canRead();
    }
}
