package net.compitek.apiparser.engine;

import java.util.function.Function;

/**
 * Created by Evgene on 05.04.2016.
 */
public class ResolverFactory<T> {
    ITaskQueue taskQueue;
    ProxySource proxySource;
    Function<T, Boolean> persister;
    Class<T> entityClass;
    int requestTimeout;


    public ResolverFactory(Class<T> entityClass,ITaskQueue taskQueue, ProxySource proxySource, int requestTimeout, Function<T, Boolean> persister) {
        this.entityClass=entityClass;
        this.taskQueue = taskQueue;
        this.proxySource = proxySource;
        this.requestTimeout = requestTimeout;
        this.persister = persister;
    }

    public Resolver<T> getResolver() {
        return new Resolver<>(entityClass,taskQueue, proxySource, requestTimeout, persister);
    }

}
