package net.compitek.apiparser.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Incapsulate queue for primarily and failed tasks.
 * <p>
 * Created by Evgene on 29.03.2016.
 */
public class TaskQueue implements ITaskQueue {
    private Logger logger = LoggerFactory.getLogger(TaskQueue.class);
    private final Thread taskSourceThread;
    private BlockingQueue<Task> primaryQueue;
    private BlockingQueue<Task> repeatedQueue;


    public TaskQueue(TaskSource taskSource) {
        this(taskSource, 1000, 300);
    }

    public TaskQueue(TaskSource taskSource, int queueSize) {
        this(taskSource, queueSize, queueSize);
    }

    public TaskQueue(TaskSource taskSource, int primaryQueueSize, int repeatedQueueSize) {
        this.primaryQueue = new LinkedBlockingDeque<Task>(primaryQueueSize);
        this.repeatedQueue = new LinkedBlockingDeque<Task>(repeatedQueueSize);
        taskSourceThread = new Thread(() -> taskSource.getTaskStream().forEach(this::putPrimarily), "taskSource_thread");
        taskSourceThread.start();
    }

    Thread getTaskSourceThread() {
        return taskSourceThread;
    }

    /**
     * wait until task source insert all tasks in taskQueue;
     */
    @Override
     public void waitTasksOver() {
        try {
            taskSourceThread.join();
        } catch (InterruptedException e) {
            logger.info("thread from pool is interrupted");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Retrieves and removes the task of queue, prioritised
     *
     * @return
     * @throws InterruptedException
     */
    @Override
    public synchronized Task getTask(){
        for (;;){
            if (!repeatedQueue.isEmpty()) {
                return repeatedQueue.remove();
            } else if (!primaryQueue.isEmpty()) {
                return primaryQueue.remove();
            } else if (taskSourceThread.isAlive()) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    logger.info("thread from pool is interrupted");
                    Thread.currentThread().interrupt();
                }
            }
            else /*if (!taskSourceThread.isAlive())*/
            {
                try {
                    wait();
                } catch (InterruptedException e) {
                    logger.info("thread from pool is interrupted");
                    Thread.currentThread().interrupt();
                }

            }
        }
    }

    /**
     * Inserts the specified (should be new) task into this queue, notify any waiting thread that queues not empty.
     *
     * @param task the task to add
     * @throws InterruptedException
     */
    @Override
    public synchronized void putPrimarily(Task task){
        try {
            primaryQueue.put(task);
        } catch (InterruptedException e) {
            logger.info("TaskSource thread interruption");
            Thread.currentThread().interrupt();
        }
        notify();
    }

    /**
     * Inserts the specified (should be already failed) task into this queue, notify any waiting thread that queues not empty.
     *
     * @param task the task to add
     * @throws InterruptedException
     */
    @Override
    public synchronized void putRepeated(Task task){
        try {
            repeatedQueue.put(task);
        } catch (InterruptedException e) {
            //interrupt one thread from pool
            logger.info("thread from pool is interrupted");
            Thread.currentThread().interrupt();
        }
        notify();
    }



    public synchronized boolean isEmpty() {
        return primaryQueue.isEmpty() && repeatedQueue.isEmpty();
    }

}
