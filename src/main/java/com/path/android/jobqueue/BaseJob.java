package com.path.android.jobqueue;

import com.path.android.jobqueue.log.JqLog;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Deprecated
public abstract class BaseJob implements Serializable {
    public static final int DEFAULT_RETRY_LIMIT = 20;
    private transient int currentRunCount;
    private String groupId;
    private boolean persistent;
    private boolean requiresNetwork;

    public abstract void onAdded();

    protected abstract void onCancel();

    public abstract void onRun() throws Throwable;

    protected abstract boolean shouldReRunOnThrowable(Throwable th);

    protected BaseJob(boolean requiresNetwork) {
        this(requiresNetwork, false, null);
    }

    protected BaseJob(String groupId) {
        this(false, false, groupId);
    }

    protected BaseJob(boolean requiresNetwork, String groupId) {
        this(requiresNetwork, false, groupId);
    }

    public BaseJob(boolean requiresNetwork, boolean persistent) {
        this(requiresNetwork, persistent, null);
    }

    protected BaseJob(boolean requiresNetwork, boolean persistent, String groupId) {
        this.requiresNetwork = requiresNetwork;
        this.persistent = persistent;
        this.groupId = groupId;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeBoolean(this.requiresNetwork);
        oos.writeObject(this.groupId);
        oos.writeBoolean(this.persistent);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        this.requiresNetwork = ois.readBoolean();
        this.groupId = (String) ois.readObject();
        this.persistent = ois.readBoolean();
    }

    public final boolean isPersistent() {
        return this.persistent;
    }

    public final boolean safeRun(int currentRunCount) {
        this.currentRunCount = currentRunCount;
        if (JqLog.isDebugEnabled()) {
            JqLog.d("running job %s", getClass().getSimpleName());
        }
        boolean reRun = false;
        try {
            onRun();
            if (JqLog.isDebugEnabled()) {
                JqLog.d("finished job %s", getClass().getSimpleName());
            }
            if (null != null) {
                return false;
            }
            if (false) {
                try {
                    onCancel();
                } catch (Throwable th) {
                }
            }
            return true;
        } catch (Throwable th2) {
        }
        if (reRun) {
            return false;
        }
        if (1 != null) {
            onCancel();
        }
        return true;
    }

    protected int getCurrentRunCount() {
        return this.currentRunCount;
    }

    public final boolean requiresNetwork() {
        return this.requiresNetwork;
    }

    public final String getRunGroupId() {
        return this.groupId;
    }

    protected int getRetryLimit() {
        return 20;
    }
}
