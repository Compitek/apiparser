package net.compitek.apiparser.engine;

/**
 * Created by Evgene on 30.03.2016.
 */
public class Task {

    private int failsMaxCounter = 3;
    private int failsCounter = 0;
    private String requestPath;

    public Task(String requestPath) {
        this.requestPath = requestPath == null ? "" : requestPath;

    }

    public Task(String requestPath, int failsMaxCounter) {
        this(requestPath);
        this.failsMaxCounter = failsMaxCounter < 1 ? 1 : failsMaxCounter;
    }

    public Task(int failsMaxCounter, int failsCounter, String requestPath) {
        this.failsMaxCounter = failsMaxCounter;
        this.failsCounter = failsCounter;
        this.requestPath = requestPath;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public boolean increaseFailsCounter() {
        return failsCounter++ < failsMaxCounter;
    }

}
