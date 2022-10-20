package galeev;

import org.jetbrains.annotations.NotNull;

public class MyTask extends Task {
    public MyTask(int producerID, int taskNumber, int waitTime) {
        super(producerID, taskNumber, waitTime);
    }

    @Override
    public @NotNull String toString() {
        return getClass().getName() + "_withId_" + producerID + "_number_" + taskNumber;
    }

    @Override
    public int compareTo(@NotNull Task o) {
        if (producerID < o.producerID) {
            return -1;
        }
        if (producerID > o.producerID) {
            return 1;
        }
        return taskNumber - o.taskNumber;
    }
}
