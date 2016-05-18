package net.compitek.apiparser.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Evgene on 05.04.2016.
 */
public class Executor<T> implements Callable<Long> {
    private Logger logger = LoggerFactory.getLogger(Executor.class);
    private ScheduledExecutorService threadPool;
    private TaskQueueWrapper taskQueueWrapper;
    private ResolverFactory resolverFactory;
    private int poolSize;
    private int separatingDelay;


    /**
     * ScheduledExecutorService wrapper
     *
     * @param entityClass yourEntity.class
     * @param poolSize - number of resolvering objects. Some more than requests wich your computer can generate during "gettingDelay" period
     * @param taskQueue - buffer of new and failed tasks
     * @param proxySource source of Proxy objects
     * @param persister for persist entity
     * @param requestTimeout timeout for getting request result
     * @param separatingDelay delay between requests in milliseconds
     */
    public Executor(Class<T> entityClass, int poolSize, TaskQueue taskQueue, ProxySource proxySource,
            int requestTimeout, int separatingDelay, Function<T, Boolean> persister) {

        this.poolSize = poolSize;
        threadPool = newScheduledThreadPool(poolSize);
        this.taskQueueWrapper = new TaskQueueWrapper(taskQueue);
        this.separatingDelay = separatingDelay;
        this.resolverFactory = new ResolverFactory<>(entityClass,taskQueueWrapper, proxySource, requestTimeout, persister);
    }

    @Override
    public synchronized Long call() throws Exception {
        long start = currentTimeMillis();
        for (int i = 0; i < poolSize; i++) {
            threadPool.scheduleWithFixedDelay(resolverFactory.getResolver(), 0, separatingDelay, MILLISECONDS);
            //scheduledFutureList.add(threadPool.scheduleWithFixedDelay(resolverFactory.getResolver(),0,separatingDelay, TimeUnit.MILLISECONDS ));
        }
        taskQueueWrapper.waitTasksOver();
        //wait untill taskQueueWrapper.getTask() notify it

        synchronized (taskQueueWrapper){
            while (!taskQueueWrapper.isTasksResolved()) {
                taskQueueWrapper.wait();
                logger.info("notified");
            }
        }



        return currentTimeMillis() - start;
    }


    private class TaskQueueWrapper implements ITaskQueue {
        private TaskQueue taskQueue;
        private volatile int taskCounter = 0;
        private boolean isTaskSourceJoined = false;

        public TaskQueueWrapper(TaskQueue taskQueue) {
            this.taskQueue = taskQueue;
        }



        public void waitTasksOver() {
            //wait until task source insert all tasks in taskQueue;
            taskQueue.waitTasksOver();
            isTaskSourceJoined = true;
        }

        public  Task getTask() {
            taskCounter++;
            if (isTasksResolved()) {
                synchronized (taskQueueWrapper){
                    this.notify();
                }
            }
            Task task = taskQueue.getTask();
            taskCounter--;
            return task;
        }

        public boolean isTasksResolved(){
            return isTaskSourceJoined && taskCounter == poolSize && taskQueue.isEmpty();
        }

        public void putPrimarily(Task task) {
            taskQueue.putPrimarily(task);
        }


        public void putRepeated(Task task) {
            taskQueue.putRepeated(task);
        }
    }


}
