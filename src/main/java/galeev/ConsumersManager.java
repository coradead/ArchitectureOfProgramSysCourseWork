package galeev;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ArrayBlockingQueue;

public abstract class ConsumersManager {
    protected final Consumer[] consumers;
    protected final Queue<Task> taskQueue;
    protected final Object threadDoneCondition = new Object();
    protected int lastIndex = 0;

    public ConsumersManager(int consumersAmount, Queue<Task> taskQueue) {
        consumers = new Consumer[consumersAmount];
        for (int i = 0; i < consumers.length; i++) {
            (consumers[i] = new Consumer(threadDoneCondition)).start();
        }
        this.taskQueue = taskQueue;
    }

    void start() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            Consumer consumer = chooseNewConsumer();
            if (consumer != null) {
                consumer.consumerQueue.offer(taskQueue.take());
            }
            // Мне лень делать это через state, поэтому просто будем просыпаться.
            // Может быть такое, что пока мы идем по циклу первый уже закончил выполнение, и мы не увидим это,
            // и кондишн уже за нотифаил, хотя в ожидание мы еще не встали
            threadDoneCondition.wait(1000);
        }
    }

    /**
     * @return free {@link ConsumersManager#consumers}' element, or null when there is no free consumers
     */
    @Nullable
    protected abstract Consumer chooseNewConsumer();

    //  Нужен ли generic?
    public static class Consumer extends Thread {

        protected final Object threadDoneCondition;
        // Currently, working with object in this queue
        protected final ArrayBlockingQueue<Task> consumerQueue;

        public Consumer(Object threadDoneCondition) {
            this.threadDoneCondition = threadDoneCondition;
            consumerQueue = new ArrayBlockingQueue<>(1);
        }

        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Task task = consumerQueue.take();
                    sleep(task.waitTime);
                } catch (InterruptedException e) {
                    // FIXME
                    e.printStackTrace();
                }
                threadDoneCondition.notify();
            }
        }

        public boolean isWorking() {
            return !consumerQueue.isEmpty();
        }
    }
}
