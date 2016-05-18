package net.compitek.apiparser.engine;

        import org.junit.After;
        import org.junit.Before;
        import org.junit.Test;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import java.util.*;
        import java.util.function.Supplier;
        import java.util.stream.Collectors;
        import java.util.stream.Stream;

        import static org.junit.Assert.*;

/**
 * Created by Evgene on 24.04.2016.
 */
public class TaskSourceTest {
    Logger logger = LoggerFactory.getLogger(TaskSourceTest.class);


    TaskSource taskSource;

    @Before
    public void setUp() throws Exception {
        taskSource = new TaskSource(new String[]{"http://gturnquist-quoters.cfapps.io/api/random"});
    }

    @After
    public void tearDown() throws Exception {
        taskSource = null;
        System.gc();
    }

    @Test
    public void testGetTaskStream() throws Exception {
        Stream<Task> taskStream = taskSource.getTaskStream();
        taskStream.forEach((t) -> logger.info(t.getRequestPath()));
    }

    @Test
    public void testConstructors() throws Exception {
        TaskSource taskSource;
        taskSource = new TaskSource(new String[]{"http://gturnquist-quoters.cfapps.io/api/random"});
        assertNotNull(taskSource.getTaskStream());
        taskSource = new TaskSource(taskSource.getTaskStream());
        assertNotNull(taskSource.getTaskStream());
        taskSource = new TaskSource(taskSource.getTaskStream().map((t) -> t.getRequestPath()).collect(Collectors.toCollection(ArrayList::new)));
        assertNotNull(taskSource.getTaskStream());

    }
}
