package galeev;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Producer {
    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);

    public final int producerId;
    protected final AtomicInteger newTaskNumber = new AtomicInteger(0);

    protected Producer(int producerId) {
        this.producerId = producerId;
    }

    @NotNull
    public static Thread buildNewThread(@NotNull Queue<Task> queue, @NotNull Producer producer) {
        LOG.info("Thread starts working with " + producer.getProducerName());
        return new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                queue.put(producer.getNewTask());
            }
        }, "Thread_" + producer.getProducerName());
    }

    public abstract Task getNewTask();

    @NotNull
    public String getProducerName() {
        return this.getClass().getName() + "_" + producerId;
    }

}
