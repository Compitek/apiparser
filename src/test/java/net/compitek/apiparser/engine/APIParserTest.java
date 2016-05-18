package net.compitek.apiparser.engine;

import resources.Entity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.FutureTask;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by Evgene on 15.05.2016.
 */
public class APIParserTest {
    private Logger logger = LoggerFactory.getLogger(APIParserTest.class);

    APIParser<Entity> apiParser;
    private int taskNumber = 7;
    private int poolSize = 3;
    int requestTimeout = 3000;
    private int separatingDelay=3000;

    @Before
    public void setUp() throws Exception {
        Stream<Task> taskStream=
                Stream.generate(()->new Task("http://gturnquist-quoters.cfapps.io/api/random"))
                        .limit(taskNumber);
        TaskSource taskSource = new TaskSource(taskStream);
        /*
        Path proxyPath = Paths.get("src\\test\\java\\resources\\proxylist.txt");
        ProxySource proxySource = new ProxySource(proxyPath);
        */
        ProxySource proxySource = Mockito.mock(ProxySource.class);
        Mockito.when(proxySource.getProxy()).thenReturn(null);
        Function<Entity, Boolean> persister= e-> {
            logger.info(e.toString());
            return true ;
        };

        try {
            apiParser = new APIParser<>(
                    Entity.class,
                    proxySource,
                    taskSource, poolSize, requestTimeout, separatingDelay,
                    (entity) -> {return true;});
        } catch (IOException e) {
            logger.debug(e.toString());
            return;
        }
    }

    @After
    public void tearDown() throws Exception {
        apiParser=null;
        System.gc();
    }

    @Test
    public void testCall() throws Exception {
        Assert.assertNotNull(apiParser);

        FutureTask<Long> futureTask = new FutureTask<>(apiParser);
        Thread thread = new Thread(futureTask, "ApiParser_DaemonThread_callable");
        thread.setDaemon(true);
        thread.start();
        Long period = futureTask.get();
        Assert.assertNotNull(period);
        Assert.assertTrue(period>(((taskNumber-1)/poolSize + 1)*separatingDelay));
        logger.info("period={}",period);
    }

/*    @Test
    public void testRun() throws Exception {
        Assert.assertNotNull(apiParser);
        Thread thread = new Thread(apiParser, "ApiParser_DaemonThread");
        thread.setDaemon(true);
        thread.start();

    }*/
}