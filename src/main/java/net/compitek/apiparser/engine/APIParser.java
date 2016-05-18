package net.compitek.apiparser.engine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Created by Evgene on 31.03.2016.
 */
public class APIParser<T> implements Runnable, Callable<Long> {
    private Logger logger = LoggerFactory.getLogger(APIParser.class);
    private Executor<T> executor;


    public APIParser(Class<T> entityClass, Path proxyPath, TaskSource taskSource, int poolSize,
                     int requestTimeout, int separatingDelay,  Function<T, Boolean> persister)
            throws IllegalArgumentException, IOException, NoSuchElementException {
        this(entityClass,
                new ProxySource(proxyPath),
                taskSource,poolSize, requestTimeout,separatingDelay,  persister);
    }

    public APIParser(Class<T> entityClass, ProxySource proxySource, TaskSource taskSource, int poolSize,
                     int requestTimeout, int separatingDelay,  Function<T, Boolean> persister)
            throws IllegalArgumentException, IOException, NoSuchElementException {
        TaskQueue taskQueue = new TaskQueue(taskSource);
        //ProxySource proxySource = new ProxySource(proxyPath);
        this.executor = new Executor<>(entityClass,poolSize, taskQueue, proxySource, requestTimeout, separatingDelay, persister);
    }

    @Override
    public Long call() throws Exception {
        FutureTask<Long> futureTask = new FutureTask<>(executor);
        Thread executorThread = new Thread(futureTask, "executor_thread");
        executorThread.start();
        try {
            return futureTask.get();
        }catch (InterruptedException e) {
            logger.info("APIParser thread is interrupted");
            Thread.currentThread().interrupt();
        }
        return null;
    }

    @Override
    public void run() {
        try {
            call();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
