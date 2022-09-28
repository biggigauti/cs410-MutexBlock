public class Mutex {
    private int acquireCount;
    private Thread lockOwner;
    private boolean lock;

    void MutexBlock() {
        acquireCount = 0;
        lockOwner = null;
    }

    public synchronized void acquire() {
        final Thread me = Thread.currentThread();

        // If someone else has the lock, wait
        while (lockOwner != null && lockOwner != me) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {} //ignore catch because we are done waiting?
        }
        // My precious
        lockOwner = me;
        acquireCount++;
    }

    public synchronized void release() {
        final Thread me = Thread.currentThread();
        if (lockOwner == null || lockOwner != me) {
            throw new IllegalStateException("Attempt to release a non-owned Mutex");
        }
        lock = false;
        acquireCount--;
        if (acquireCount == 0) {
            lockOwner = null;
            this.notifyAll();
        }
    }

    public synchronized boolean getLockStatus() {
        return lockOwner != null;
    }
}