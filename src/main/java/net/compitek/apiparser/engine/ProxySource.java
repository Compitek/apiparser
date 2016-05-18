package net.compitek.apiparser.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.util.URLUtil;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by Evgene on 31.03.2016.
 */
public class ProxySource {
    private Logger logger = LoggerFactory.getLogger(ProxySource.class);
    private BlockingQueue<Proxy> repeatedProxy = new LinkedBlockingQueue<>();
    private static final int  defaultPort = 8080;

    public ProxySource(Path path) throws IllegalArgumentException, IOException {
        if (!(Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path))) {
            throw new IllegalArgumentException("ProxySource: wrong proxy-list file path");
        }
        Function<String,Proxy > stringToProxy = this::stringToProxy;
        Consumer<Proxy> addRepeatedProxy =  repeatedProxy::add;
        Files.lines(path).map(stringToProxy).forEach(addRepeatedProxy);
    }

    private Proxy stringToProxy(String line) {
        URI uri;
        try {
            uri = new URI("//" + line);
        } catch (URISyntaxException e) {
            logger.info("wrong proxy path port format." + e.toString());
            return null;
        }
        int port = uri.getPort();
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(uri.getHost(), port > 0 ? port : defaultPort));
    }


    public synchronized Proxy getProxy() throws NoSuchElementException {
        if (!repeatedProxy.isEmpty())
            return repeatedProxy.poll();
        throw new NoSuchElementException();


    }

    public synchronized void returnFailedProxy(Proxy failedProxy) {
        repeatedProxy.add(failedProxy);
    }
}

