package com.j256.ormlite.dao;

import com.j256.ormlite.field.FieldType;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LazyForeignCollection<T, ID> extends BaseForeignCollection<T, ID> implements ForeignCollection<T>, Serializable {
    private static final long serialVersionUID = -5460708106909626233L;
    private transient CloseableIterator<T> lastIterator;

    public LazyForeignCollection(Dao<T, ID> dao, Object parent, Object parentId, FieldType foreignFieldType, String orderColumn, boolean orderAscending) {
        super(dao, parent, parentId, foreignFieldType, orderColumn, orderAscending);
    }

    public CloseableIterator<T> iterator() {
        return closeableIterator(-1);
    }

    public CloseableIterator<T> iterator(int flags) {
        return closeableIterator(flags);
    }

    public CloseableIterator<T> closeableIterator() {
        return closeableIterator(-1);
    }

    public CloseableIterator<T> closeableIterator(int flags) {
        try {
            return iteratorThrow(flags);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not build lazy iterator for " + this.dao.getDataClass(), e);
        }
    }

    public CloseableIterator<T> iteratorThrow() throws SQLException {
        return iteratorThrow(-1);
    }

    public CloseableIterator<T> iteratorThrow(int flags) throws SQLException {
        this.lastIterator = seperateIteratorThrow(flags);
        return this.lastIterator;
    }

    public CloseableWrappedIterable<T> getWrappedIterable() {
        return getWrappedIterable(-1);
    }

    public CloseableWrappedIterable<T> getWrappedIterable(final int flags) {
        return new CloseableWrappedIterableImpl(new CloseableIterable<T>() {
            public CloseableIterator<T> iterator() {
                return closeableIterator();
            }

            public CloseableIterator<T> closeableIterator() {
                try {
                    return LazyForeignCollection.this.seperateIteratorThrow(flags);
                } catch (Exception e) {
                    throw new IllegalStateException("Could not build lazy iterator for " + LazyForeignCollection.this.dao.getDataClass(), e);
                }
            }
        });
    }

    public void closeLastIterator() throws SQLException {
        if (this.lastIterator != null) {
            this.lastIterator.close();
            this.lastIterator = null;
        }
    }

    public boolean isEager() {
        return false;
    }

    public int size() {
        CloseableIterator<T> iterator = iterator();
        int sizeC = 0;
        while (iterator.hasNext()) {
            try {
                iterator.moveToNext();
                sizeC++;
            } finally {
                try {
                    iterator.close();
                } catch (SQLException e) {
                }
            }
        }
        return sizeC;
    }

    public boolean isEmpty() {
        CloseableIterator<T> iterator = iterator();
        try {
            boolean z = !iterator.hasNext();
            return z;
        } finally {
            try {
                iterator.close();
            } catch (SQLException e) {
            }
        }
    }

    public boolean contains(Object obj) {
        boolean z;
        CloseableIterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            try {
                if (iterator.next().equals(obj)) {
                    z = true;
                    break;
                }
            } finally {
                try {
                    iterator.close();
                } catch (SQLException e) {
                }
            }
        }
        z = false;
        try {
            iterator.close();
        } catch (SQLException e2) {
        }
        return z;
    }

    public boolean containsAll(Collection<?> collection) {
        Set<Object> leftOvers = new HashSet(collection);
        CloseableIterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            try {
                leftOvers.remove(iterator.next());
            } finally {
                try {
                    iterator.close();
                } catch (SQLException e) {
                }
            }
        }
        boolean isEmpty = leftOvers.isEmpty();
        return isEmpty;
    }

    public boolean remove(Object data) {
        boolean z;
        CloseableIterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            try {
                if (iterator.next().equals(data)) {
                    iterator.remove();
                    z = true;
                    break;
                }
            } finally {
                try {
                    iterator.close();
                } catch (SQLException e) {
                }
            }
        }
        z = false;
        try {
            iterator.close();
        } catch (SQLException e2) {
        }
        return z;
    }

    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        CloseableIterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            try {
                if (collection.contains(iterator.next())) {
                    iterator.remove();
                    changed = true;
                }
            } finally {
                try {
                    iterator.close();
                } catch (SQLException e) {
                }
            }
        }
        return changed;
    }

    public Object[] toArray() {
        List<T> items = new ArrayList();
        CloseableIterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            try {
                items.add(iterator.next());
            } finally {
                try {
                    iterator.close();
                } catch (SQLException e) {
                }
            }
        }
        Object[] toArray = items.toArray();
        return toArray;
    }

    public <E> E[] toArray(E[] array) {
        Throwable th;
        int itemC = 0;
        CloseableIterator<T> iterator = iterator();
        List<E> items = null;
        while (iterator.hasNext()) {
            List<E> items2;
            E castData = iterator.next();
            if (itemC >= array.length) {
                if (items == null) {
                    items2 = new ArrayList();
                    try {
                        for (E arrayData : array) {
                            items2.add(arrayData);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } else {
                    items2 = items;
                }
                items2.add(castData);
            } else {
                try {
                    array[itemC] = castData;
                    items2 = items;
                } catch (Throwable th3) {
                    th = th3;
                    items2 = items;
                }
            }
            itemC++;
            items = items2;
        }
        try {
            iterator.close();
        } catch (SQLException e) {
        }
        if (items != null) {
            return items.toArray(array);
        }
        if (itemC >= array.length - 1) {
            return array;
        }
        array[itemC] = null;
        return array;
        try {
            iterator.close();
        } catch (SQLException e2) {
        }
        throw th;
        throw th;
    }

    public int updateAll() {
        throw new UnsupportedOperationException("Cannot call updateAll() on a lazy collection.");
    }

    public int refreshAll() {
        throw new UnsupportedOperationException("Cannot call updateAll() on a lazy collection.");
    }

    public int refreshCollection() {
        return 0;
    }

    public boolean equals(Object other) {
        return super.equals(other);
    }

    public int hashCode() {
        return super.hashCode();
    }

    private CloseableIterator<T> seperateIteratorThrow(int flags) throws SQLException {
        if (this.dao != null) {
            return this.dao.iterator(getPreparedQuery(), flags);
        }
        throw new IllegalStateException("Internal DAO object is null.  Lazy collections cannot be used if they have been deserialized.");
    }
}
