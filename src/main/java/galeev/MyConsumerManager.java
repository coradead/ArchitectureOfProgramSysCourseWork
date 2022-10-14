package galeev;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyConsumerManager extends ConsumersManager {
    public MyConsumerManager(int consumersAmount, @NotNull Queue<Task> taskQueue) {
        super(consumersAmount, taskQueue);
    }

    @Override
    protected @Nullable Consumer chooseNewConsumer() {
        for (int i = lastIndex; i != lastIndex - 1; i++) {
            if (i == consumers.length) i = 0;
            if (!consumers[i].isWorking()) {
                lastIndex = (i + 1) % consumers.length;
                return consumers[i];
            }
        }
        return null;
    }
}
