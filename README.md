# apiparser

Collection classes for grabbing a third party API through proxies in multithreaded mode 
(with delay between requests through each proxy).

At the entrance requiring
1. a list of the proxy(in a file, one string - one proxy adress)
2. a stream of requests (for example, in a file, one string - one request)
3. an Entity class
4. a permissing object, which will saving entities in DB.

API should receive GET-requests, and response results as JSON.
Jar-file in out/artifacts/apiparser_jar/

Details:
  Intellij IDEA project
  Java8
  Jars included:	
    jackson 2.7.3
    slf4j 1.7.21
    for Spring RestTemplate:
      springframework 4.2.5.RELEASE 
    for tests:
      junit 4.12
      mockito 1.10.19
      
