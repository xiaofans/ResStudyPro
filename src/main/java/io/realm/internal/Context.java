package io.realm.internal;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

public class Context {
    private List<Long> abandonedQueries = new ArrayList();
    private List<Long> abandonedTableViews = new ArrayList();
    private List<Long> abandonedTables = new ArrayList();
    private boolean isFinalized = false;
    ReferenceQueue<NativeObject> referenceQueue = new ReferenceQueue();
    List<Reference<?>> rowReferences = new ArrayList();

    public void executeDelayedDisposal() {
        synchronized (this) {
            int i;
            for (i = 0; i < this.abandonedTables.size(); i++) {
                Table.nativeClose(((Long) this.abandonedTables.get(i)).longValue());
            }
            this.abandonedTables.clear();
            for (i = 0; i < this.abandonedTableViews.size(); i++) {
                TableView.nativeClose(((Long) this.abandonedTableViews.get(i)).longValue());
            }
            this.abandonedTableViews.clear();
            for (i = 0; i < this.abandonedQueries.size(); i++) {
                TableQuery.nativeClose(((Long) this.abandonedQueries.get(i)).longValue());
            }
            this.abandonedQueries.clear();
            cleanRows();
        }
    }

    public void cleanRows() {
        NativeObjectReference reference = (NativeObjectReference) this.referenceQueue.poll();
        while (reference != null) {
            UncheckedRow.nativeClose(reference.nativePointer);
            this.rowReferences.remove(reference);
            reference = (NativeObjectReference) this.referenceQueue.poll();
        }
    }

    public void asyncDisposeTable(long nativePointer, boolean isRoot) {
        if (isRoot || this.isFinalized) {
            Table.nativeClose(nativePointer);
        } else {
            this.abandonedTables.add(Long.valueOf(nativePointer));
        }
    }

    public void asyncDisposeTableView(long nativePointer) {
        if (this.isFinalized) {
            TableView.nativeClose(nativePointer);
        } else {
            this.abandonedTableViews.add(Long.valueOf(nativePointer));
        }
    }

    public void asyncDisposeQuery(long nativePointer) {
        if (this.isFinalized) {
            TableQuery.nativeClose(nativePointer);
        } else {
            this.abandonedQueries.add(Long.valueOf(nativePointer));
        }
    }

    public void asyncDisposeGroup(long nativePointer) {
        Group.nativeClose(nativePointer);
    }

    public void asyncDisposeSharedGroup(long nativePointer) {
        SharedGroup.nativeClose(nativePointer);
    }

    protected void finalize() {
        synchronized (this) {
            this.isFinalized = true;
        }
        executeDelayedDisposal();
    }
}
