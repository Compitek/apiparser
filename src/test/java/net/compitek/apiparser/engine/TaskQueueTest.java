package net.compitek.apiparser.engine;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Evgene on 25.04.2016.
 */
public class TaskQueueTest {

    TaskQueue taskQueue;


    @Before
    public void setUp() throws Exception {
        taskQueue = new TaskQueue(new TaskSource(new String[]{"http://gturnquist-quoters.cfapps.io/api/random"}));
    }

    @After
    public void tearDown() throws Exception {
        taskQueue=null;
        System.gc();
    }

    @Test
    public void testGetTaskSourceThread() throws Exception {
        Assert.assertNotNull(taskQueue.getTaskSourceThread());
    }

    @Test
    public void testWaitTasksOver() throws Exception {
        taskQueue.waitTasksOver();
    }

    @Test
    public void testGetTask() throws Exception {
        Task task = taskQueue.getTask();
        Assert.assertNotNull(task);
    }


    @Test
    public void testIsEmpty() throws Exception {
        TaskQueue taskQueue = new TaskQueue(new TaskSource(new String[]{}));
        Assert.assertTrue(taskQueue.isEmpty());
        }

    @Test
    public void testPutPrimarily() throws Exception {
        Task task = new Task("http://gturnquist-quoters.cfapps.io/api/random");
        TaskQueue taskQueue = new TaskQueue(new TaskSource(new String[]{}));
        Assert.assertTrue(taskQueue.isEmpty());
        taskQueue.putPrimarily(task);
        Assert.assertFalse(taskQueue.isEmpty());
        taskQueue.getTask();
        Assert.assertTrue(taskQueue.isEmpty());
    }

    @Test
    public void testPutRepeated() throws Exception {
        Task task = new Task("http://gturnquist-quoters.cfapps.io/api/random");
        TaskQueue taskQueue = new TaskQueue(new TaskSource(new String[]{}));
        taskQueue.putRepeated(task);
        Assert.assertFalse(taskQueue.isEmpty());
        taskQueue.getTask();
        Assert.assertTrue(taskQueue.isEmpty());
    }

}