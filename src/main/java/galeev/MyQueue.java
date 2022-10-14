package galeev;

public class MyQueue<E extends Task> extends Queue<E> {
    int newestIndex;

    public MyQueue(int capacity) {
        super(capacity);
    }

    @Override
    protected void enqueue(E e) {
        // assert lock.isHeldByCurrentThread();
        // assert lock.getHoldCount() == 1;
        // assert items[putIndex] == null;
        final Object[] items = this.items;
        items[putIndex] = e;
        newestIndex = putIndex;
        if (++putIndex == items.length) putIndex = 0;
        count++;
        notEmpty.signal();
    }

    @Override
    protected E dequeue() {
        // assert lock.isHeldByCurrentThread();
        // assert lock.getHoldCount() == 1;
        @SuppressWarnings("unchecked")
        final E[] items = (E[]) this.items;
        int smallestIndex = -1;
        // Get first not null
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                smallestIndex = i;
                break;
            }
        }

        assert items[smallestIndex] != null;

        for (int i = smallestIndex + 1; i < items.length; i++) {
            if (items[i] != null) {
                if (items[smallestIndex].compareTo(items[i]) > 0) {
                    smallestIndex = i;
                }
            }
        }
        E e = items[smallestIndex];
        items[smallestIndex] = null;
        count--;
        return e;
    }

    @Override
    protected E reject() {
        @SuppressWarnings("unchecked")
        E e = (E) items[newestIndex];
        items[newestIndex] = null;
        count--;
        return e;
    }
}
