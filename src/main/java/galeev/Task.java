package galeev;

import org.jetbrains.annotations.NotNull;

public abstract class Task implements Comparable<Task> {
    public final int producerID;
    public final int taskNumber;
    public final int waitTime;

    public Task(int producerID, int taskNumber, int waitTime) {
        this.producerID = producerID;
        this.taskNumber = taskNumber;
        this.waitTime = waitTime;
    }

    /**
     * @return unique staring name
     */
    @NotNull
    @Override
    public abstract String toString();

    @Override
    public abstract int compareTo(@NotNull Task o);
}
