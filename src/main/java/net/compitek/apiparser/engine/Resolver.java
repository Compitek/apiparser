package net.compitek.apiparser.engine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.datetime.standard.DateTimeContextHolder;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.net.Proxy;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;



/**
 * Created by Evgene on 30.03.2016.
 */
public class Resolver<T> implements Runnable {
    private Logger logger = LoggerFactory.getLogger(Resolver.class);
    private ITaskQueue taskQueue;
    private RestTemplate restTemplate;
    private Function<T, Boolean> persister;
    private ProxySource proxySource;
    private Proxy currentProxy;
    private SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
    private Class<T> entityClass;
    private int requestTimeout;


    public Resolver(Class<T> entityClass, ITaskQueue taskQueue, ProxySource proxySource, int requestTimeout, Function<T, Boolean> persister) {
        this.entityClass=entityClass;
        this.taskQueue = taskQueue;
        this.proxySource = proxySource;
        /*
        http://www.leveluplunch.com/java/tutorials/015-consuming-rest-webservice-with-spring-resttemplate/#resttemplate-with-proxy
         */
        currentProxy = proxySource.getProxy();
        clientHttpRequestFactory.setProxy(currentProxy);
        restTemplate = new RestTemplate(clientHttpRequestFactory);
        //http://stackoverflow.com/questions/28131139/springtemplate-no-suitable-httpmessageconverter-found-for-response-type
        List<MediaType> mediaTypes = Arrays.asList(
                new MediaType("application", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET),
                new MediaType("text", "javascript", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET)
        );
        restTemplate.getMessageConverters().stream()
                .filter((c)->c instanceof MappingJackson2HttpMessageConverter)
                .forEach((c)->
                        ((MappingJackson2HttpMessageConverter)c)
                                .setSupportedMediaTypes(mediaTypes)
                );
        this.requestTimeout = requestTimeout;
        this.persister = persister;

    }

    public void run(){
        Task task = taskQueue.getTask();
        Optional<Future<T>> result = this.resolve(task);
        if (result.isPresent()) {
            try {
                Date startDate = new Date();
                persister.apply(result.get().get(requestTimeout, TimeUnit.MILLISECONDS));
            } catch (ExecutionException | TimeoutException e) {
                changeProxy();
                taskQueue.putRepeated(task);
            } catch (InterruptedException e) {
                logger.info("thread from pool is interrupted");
                Thread.currentThread().interrupt();
            }
        } else {
            changeProxy();
            taskQueue.putRepeated(task);
        }
    }

    private void changeProxy() {
        proxySource.returnFailedProxy(currentProxy);
        currentProxy = proxySource.getProxy();
        clientHttpRequestFactory.setProxy(proxySource.getProxy());
    }

    private Optional<Future<T>> resolve(Task task) {
        T entity;
        try {
            //http://spring-projects.ru/guides/async-method/
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> req = new HttpEntity<>(headers);
            //ParameterizedTypeReference<T> responseType = new ParameterizedTypeReference<T>() {};
            ResponseEntity<T> exchange =
                    restTemplate.exchange(
                            task.getRequestPath(),
                            HttpMethod.GET,
                            req,
                            entityClass
                    );
            entity = exchange.getBody();

        } catch (RestClientException e) {
            if (task.increaseFailsCounter()) {
                taskQueue.putRepeated(task);
            }
            return Optional.empty();
        }
        return Optional.of(new AsyncResult<T>(entity));
    }


}
