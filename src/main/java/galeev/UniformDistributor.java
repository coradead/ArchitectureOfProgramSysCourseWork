package galeev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;

public class UniformDistributor extends Producer {
    private static final Logger LOG = LoggerFactory.getLogger(UniformDistributor.class);

    private UniformDistributor(int producerId) {
        super(producerId);
        LOG.info("Init new producer " + UniformDistributor.class.getName() + " with name " + producerId);
    }

    @Override
    public Task getNewTask() {
        int newTaskNumber = this.newTaskNumber.getAndIncrement();
        LOG.info(getProducerName() + " start producing " + newTaskNumber);
        try {
            sleep(700);
        } catch (InterruptedException e) {
            LOG.error("Sleep was interrupted when producing new task, number was: " + newTaskNumber, e);
            // FIXME
            //e.printStackTrace();
        }
        Task task = new MyTask(producerId, newTaskNumber, 1000);
        LOG.info(getProducerName() + " produced " + task);
        return task;
    }
}
