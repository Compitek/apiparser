package net.compitek.apiparser.engine;

import resources.Entity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.FutureTask;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by Evgene on 28.04.2016.
 */
public class ExecutorTest {
    private Logger logger = LoggerFactory.getLogger(ExecutorTest.class);
    private Executor<Entity> executor;
    private int taskNumber = 7;
    private int poolSize = 3;
    private int requestTimeout =3000;
    private int  separatingDelay=3000;

    @Before
    public void setUp() throws Exception {
        Stream<Task> taskStream=
                Stream.generate(()->new Task("http://gturnquist-quoters.cfapps.io/api/random"))
                        .limit(taskNumber);
        TaskSource taskSource = new TaskSource(taskStream);
        TaskQueue taskQueue = new TaskQueue(taskSource);
        ProxySource proxySource = Mockito.mock(ProxySource.class);
        Mockito.when(proxySource.getProxy()).thenReturn(null);
        Function<Entity, Boolean> persister= e-> {
            logger.info(e.toString());
            return true ;
        };
        this.executor = new Executor<>(Entity.class, poolSize, taskQueue
                , proxySource, requestTimeout,separatingDelay, persister);
    }

    @After
    public void tearDown() throws Exception {
        executor=null;
        System.gc();
    }

    @Test
    public void testCall() throws Exception {
        FutureTask<Long> futureTask = new FutureTask<Long>(executor);
        Thread executorThread = new Thread(futureTask, "executor_thread");
        executorThread.start();
        Long period = futureTask.get();

        Assert.assertTrue(period>(((taskNumber-1)/poolSize + 1)*separatingDelay));
        logger.info("period={}",period);

    }

}