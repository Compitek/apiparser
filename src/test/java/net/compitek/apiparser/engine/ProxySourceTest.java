package net.compitek.apiparser.engine;

import net.compitek.apiparser.engine.ProxySource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.JUnit4;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Evgene on 23.04.2016.
 */
public class ProxySourceTest {

    ProxySource proxySource;
    Proxy proxy;
    long lineCount;
    public ExpectedException thrown;

    @Before
    public void setUp() throws Exception {
        Path proxyPath = Paths.get("src\\test\\java\\resources\\proxylist.txt");
        lineCount = Files.lines(proxyPath).count();
        proxySource = new ProxySource(proxyPath);
        thrown= ExpectedException.none();
    }

    @After
    public void tearDown() throws Exception {
        proxySource=null;
        System.gc();
    }

    @Test
    public void testGetProxy() throws Exception {
        Set<Proxy> proxySet=new HashSet<>();
        for(int i=1; i<lineCount;i++) {
            proxy = proxySource.getProxy();
            Assert.assertNotNull(proxy);
            proxySet.add(proxy);
        }
        thrown.expect(NoSuchElementException.class);
        proxySet.stream().forEach(proxySource::returnFailedProxy);
    }

    @Test
    public void testReturnFailedProxy() throws Exception {
        URI uri = new URI("//92.46.125.177:3128");
        int port = uri.getPort();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(uri.getHost(), port));
        proxySource.returnFailedProxy(proxy);
    }
}