package com.douban.book.reader.manager;

import com.douban.book.reader.controller.TaskController;
import com.douban.book.reader.network.client.RestClient;
import com.douban.book.reader.network.exception.RestException;
import com.douban.book.reader.util.Logger;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;

public class Syncer<T> {
    private static final String TAG = Syncer.class.getSimpleName();
    private Dao<T, Object> mDao = null;
    private RestClient<T> mRestClient = null;

    public Syncer(Dao<T, Object> dao, RestClient<T> restClient, Class<T> cls) {
        this.mDao = dao;
        this.mRestClient = restClient;
    }

    public T get(Object id) throws RestException, SQLException {
        Object entity = this.mRestClient.get(id);
        this.mDao.update(entity);
        return entity;
    }

    public void asyncGet(final Object id) {
        TaskController.run(new Runnable() {
            public void run() {
                try {
                    Syncer.this.get(id);
                } catch (Exception e) {
                    Logger.e(Syncer.TAG, e, "Failed to get object %s from server.", id);
                }
            }
        });
    }

    public void add(T entity) throws RestException, SQLException {
        this.mDao.update(this.mRestClient.post((Object) entity));
        Logger.v(TAG, "Synced to cloud: %s", entity);
    }

    public void asyncAdd(final T entity) {
        TaskController.run(new Runnable() {
            public void run() {
                try {
                    Syncer.this.add(entity);
                } catch (Exception e) {
                    Logger.e(Syncer.TAG, e, "Failed to add object %s to server.", entity);
                }
            }
        });
    }

    public void delete(Object id) throws RestException {
        this.mRestClient.delete(id);
        Logger.v(TAG, "Deleted from cloud: %s", id);
    }

    public void asyncDelete(final Object id) {
        TaskController.run(new Runnable() {
            public void run() {
                try {
                    Syncer.this.delete(id);
                } catch (Exception e) {
                    Logger.e(Syncer.TAG, e, "Failed to delete object %s from server.", id);
                }
            }
        });
    }
}
