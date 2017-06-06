package com.path.android.jobqueue.nonPersistentQueue;

import com.path.android.jobqueue.JobHolder;
import java.util.Collection;
import java.util.Comparator;

public abstract class MergedQueue implements JobSet {
    final Comparator<JobHolder> comparator;
    JobSet queue0;
    JobSet queue1;
    final Comparator<JobHolder> retrieveComparator;

    protected enum SetId {
        S0,
        S1
    }

    protected abstract JobSet createQueue(SetId setId, int i, Comparator<JobHolder> comparator);

    protected abstract SetId decideQueue(JobHolder jobHolder);

    public MergedQueue(int initialCapacity, Comparator<JobHolder> comparator, Comparator<JobHolder> retrieveComparator) {
        this.comparator = comparator;
        this.retrieveComparator = retrieveComparator;
        this.queue0 = createQueue(SetId.S0, initialCapacity, comparator);
        this.queue1 = createQueue(SetId.S1, initialCapacity, comparator);
    }

    protected JobHolder pollFromQueue(SetId queueId, Collection<String> excludeGroupIds) {
        if (queueId == SetId.S0) {
            return this.queue0.poll(excludeGroupIds);
        }
        return this.queue1.poll(excludeGroupIds);
    }

    protected JobHolder peekFromQueue(SetId queueId, Collection<String> excludeGroupIds) {
        if (queueId == SetId.S0) {
            return this.queue0.peek(excludeGroupIds);
        }
        return this.queue1.peek(excludeGroupIds);
    }

    public boolean offer(JobHolder jobHolder) {
        if (decideQueue(jobHolder) == SetId.S0) {
            return this.queue0.offer(jobHolder);
        }
        return this.queue1.offer(jobHolder);
    }

    public JobHolder poll(Collection<String> excludeGroupIds) {
        JobHolder delayed = this.queue0.peek(excludeGroupIds);
        if (delayed == null) {
            return this.queue1.poll(excludeGroupIds);
        }
        if (decideQueue(delayed) != SetId.S0) {
            this.queue0.remove(delayed);
            this.queue1.offer(delayed);
            return poll(excludeGroupIds);
        }
        JobHolder nonDelayed = this.queue1.peek(excludeGroupIds);
        if (nonDelayed == null) {
            this.queue0.remove(delayed);
            return delayed;
        } else if (decideQueue(nonDelayed) != SetId.S1) {
            this.queue0.offer(nonDelayed);
            this.queue1.remove(nonDelayed);
            return poll(excludeGroupIds);
        } else if (this.retrieveComparator.compare(delayed, nonDelayed) == -1) {
            this.queue0.remove(delayed);
            return delayed;
        } else {
            this.queue1.remove(nonDelayed);
            return nonDelayed;
        }
    }

    public JobHolder peek(Collection<String> excludeGroupIds) {
        while (true) {
            JobHolder delayed = this.queue0.peek(excludeGroupIds);
            JobHolder nonDelayed;
            if (delayed == null || decideQueue(delayed) == SetId.S0) {
                nonDelayed = this.queue1.peek(excludeGroupIds);
                if (nonDelayed != null && decideQueue(nonDelayed) != SetId.S1) {
                    this.queue0.offer(nonDelayed);
                    this.queue1.remove(nonDelayed);
                } else if (delayed == null) {
                    return nonDelayed;
                } else {
                    if (nonDelayed == null) {
                        return delayed;
                    }
                    if (this.retrieveComparator.compare(delayed, nonDelayed) != -1) {
                        return delayed;
                    }
                    return nonDelayed;
                }
            }
            this.queue1.offer(delayed);
            this.queue0.remove(delayed);
        }
        if (delayed == null) {
            return nonDelayed;
        }
        if (nonDelayed == null) {
            return delayed;
        }
        if (this.retrieveComparator.compare(delayed, nonDelayed) != -1) {
            return nonDelayed;
        }
        return delayed;
    }

    public void clear() {
        this.queue1.clear();
        this.queue0.clear();
    }

    public boolean remove(JobHolder holder) {
        return this.queue1.remove(holder) || this.queue0.remove(holder);
    }

    public int size() {
        return this.queue0.size() + this.queue1.size();
    }

    public CountWithGroupIdsResult countReadyJobs(SetId setId, long now, Collection<String> excludeGroups) {
        if (setId == SetId.S0) {
            return this.queue0.countReadyJobs(now, excludeGroups);
        }
        return this.queue1.countReadyJobs(now, excludeGroups);
    }

    public CountWithGroupIdsResult countReadyJobs(SetId setId, Collection<String> excludeGroups) {
        if (setId == SetId.S0) {
            return this.queue0.countReadyJobs(excludeGroups);
        }
        return this.queue1.countReadyJobs(excludeGroups);
    }

    public JobHolder findById(long id) {
        JobHolder q0 = this.queue0.findById(id);
        return q0 == null ? this.queue1.findById(id) : q0;
    }
}
