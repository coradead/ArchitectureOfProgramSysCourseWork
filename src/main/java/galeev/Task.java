package galeev;

public abstract class Task implements Comparable<Task> {
    public final int producerID;
    public final int taskNumber;
    public final int waitTime;

    public Task(int producerID, int taskNumber, int waitTime) {
        this.producerID = producerID;
        this.taskNumber = taskNumber;
        this.waitTime = waitTime;
    }

    @Override
    public abstract String toString();
}
