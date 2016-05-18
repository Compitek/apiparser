package net.compitek.apiparser.engine;

import resources.Entity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * Created by Evgene on 19.04.2016.
 */
public class ResolverTest {
    Logger logger = LoggerFactory.getLogger(ResolverTest.class);
    Resolver<Entity> resolver;
    private int requestTimeout = 3000;

    @Before
    public void setUp() throws Exception {
        TaskSource taskSource = new TaskSource(new String[]{"http://gturnquist-quoters.cfapps.io/api/random"});
        TaskQueue taskQueue = new TaskQueue(taskSource);
        ProxySource proxySource = Mockito.mock(ProxySource.class);
        Mockito.when(proxySource.getProxy()).thenReturn(null);
        Function<Entity, Boolean> persister= e-> {
            logger.info(e.toString());
            return true ;
        };
        resolver = new Resolver<>(Entity.class, taskQueue, proxySource, requestTimeout, persister);
    }

    @After
    public void tearDown() throws Exception {
        resolver = null;
        System.gc();
    }

    @Test
    public void testRun() throws Exception {
        resolver.run();
    }



}