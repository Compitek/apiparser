package net.compitek.apiparser.engine;

import org.springframework.web.util.UriTemplate;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by Evgene on 30.03.2016.
 */
public class TaskSource {
    private Stream<Task> taskStream;

    public Stream<Task> getTaskStream() {
        return taskStream;
    }

    public TaskSource(String[] taskPaths) {
        this.taskStream = Arrays.stream(taskPaths).map((path) -> new Task(path));
    }

    public TaskSource(Stream<Task> taskStream) {
        this.taskStream = taskStream;
    }


    public TaskSource(Collection<String> taskPaths) {
        this.taskStream = taskPaths.stream().map((path) -> new Task(path));
    }

/*    private TaskSource(UriTemplate uriTemplate, Stream<String> argStream) {
        this.taskStream = argStream.map((argument) -> new Task(uriTemplate.expand(argument).toString()));
    }

    public TaskSource(UriTemplate uriTemplate, Object[][] arguments) {
        LinkedList<String> paths = new LinkedList<>();
        for (int i = 0; i < arguments.length; i++) {
            paths.add(uriTemplate.expand(arguments[i]).toString());
        }
        this.taskStream = paths.stream().map((path) -> new Task(path));
    }*/

}
