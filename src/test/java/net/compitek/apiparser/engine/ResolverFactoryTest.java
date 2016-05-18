package net.compitek.apiparser.engine;

import resources.Entity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * Created by Evgene on 28.04.2016.
 */
public class ResolverFactoryTest {
    Logger logger = LoggerFactory.getLogger(ResolverFactoryTest.class);

    ResolverFactory<Entity> resolverFactory;
    Function<Entity, Boolean> persister;
    private int requestTimeout = 3000;


    @Before
    public void setUp() throws Exception {
        TaskSource taskSource = new TaskSource(new String[]{"http://gturnquist-quoters.cfapps.io/api/random"});
        TaskQueue taskQueue = new TaskQueue(taskSource);

        ProxySource proxySource = Mockito.mock(ProxySource.class);
        Mockito.when(proxySource.getProxy()).thenReturn(null);

        persister= e-> {
            logger.info(e.toString());
            return true ;
        };

        resolverFactory = new ResolverFactory<>(Entity.class,taskQueue,proxySource,requestTimeout,persister);

    }

    @After
    public void tearDown() throws Exception {
        resolverFactory=null;
        persister=null;
        System.gc();
    }

    @Test
    public void testGetResolver() throws Exception {
        Resolver<Entity> resolver = resolverFactory.getResolver();
        Assert.assertNotNull(resolver);
    }
}