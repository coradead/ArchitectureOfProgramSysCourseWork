package galeev;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Queue<E extends Task> {
    protected final Condition notEmpty;
    protected final Object[] items;
    protected final ReentrantLock lock;
    protected int putIndex;
    protected int count;

    public Queue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.items = new Object[capacity];
        lock = new ReentrantLock(false);
        notEmpty = lock.newCondition();
    }

    /**
     * Inserts element in queue.
     * Should be at least protected, because usual user should use {@link Queue#put(Task)}.
     * Here only should be logic of extracting element.
     * There always will be place for new element.
     * @param e element to insert
     */
    protected abstract void enqueue(E e);

    /**
     * Should be at least protected, because usual user should use {@link Queue#take()}.
     * Here only should be logic of inserting element.
     * There always will be item to return.
     * @return element from queue
     */
    @NotNull
    protected abstract E dequeue();

    /**
     * Should be at least protected, because usual user should not use it.
     * Here only should be logic of rejecting element.
     * There always will be item to reject.
     * @return rejected element
     */
    @NotNull
    protected abstract E reject();

    @SuppressWarnings("unchecked")
    final E itemAt(int i) {
        return (E) items[i];
    }

    public int size() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    // Returns rejected E (null if nothing was rejected)
    @Nullable
    public E put(@NotNull E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            E rejected = null;
            // Reject policy
            if (count == items.length) {
                rejected = reject();
            }
            enqueue(e);
            return rejected;
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            return dequeue();
        } finally {
            lock.unlock();
        }
    }
}
