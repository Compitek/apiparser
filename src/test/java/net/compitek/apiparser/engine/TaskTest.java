package net.compitek.apiparser.engine;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.ranges.Range;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by Evgene on 28.04.2016.
 */
public class TaskTest {
    Task taskNull;
    Task taskSpace;
    Task taskPathed;

    Task taskNull5fails;
    Task taskSpace5fails;
    Task taskPathed5fails;

    Task taskNull0fails;
    Task taskSpace0fails;
    Task taskPathed0fails;

    Task taskNullMinus4;
    Task taskSpaceMinus4;
    Task taskPathedMinus4;

    @Before
    public void setUp() throws Exception {
        taskNull = new Task(null);
        taskSpace = new Task("");
        taskPathed = new Task("http://gturnquist-quoters.cfapps.io/api/random");

        taskNull5fails = new Task(null,5);
        taskSpace5fails = new Task("",5);
        taskPathed5fails = new Task("http://gturnquist-quoters.cfapps.io/api/random",5);

        taskNull0fails = new Task(null,0);
        taskSpace0fails = new Task("",0);
        taskPathed0fails = new Task("http://gturnquist-quoters.cfapps.io/api/random",0);

        taskNullMinus4 = new Task(null,-4);
        taskSpaceMinus4 = new Task("",-4);
        taskPathedMinus4 = new Task("http://gturnquist-quoters.cfapps.io/api/random",-4);
    }
    @After
    public void tearDown() throws Exception {
        taskNull = null;
        taskSpace = null;
        taskPathed = null;

        taskNull5fails = null;
        taskSpace5fails = null;
        taskPathed5fails = null;

        taskNull0fails = null;
        taskSpace0fails = null;
        taskPathed0fails = null;

        taskNullMinus4 = null;
        taskSpaceMinus4 = null;
        taskPathedMinus4 = null;
        System.gc();
    }

    @Test
    public void testGetRequestPath() throws Exception {
        Assert.assertNotNull(taskNull);
        Assert.assertNotNull(taskNull.getRequestPath());
        Assert.assertEquals("",taskNull.getRequestPath());

        Assert.assertNotNull(taskSpace);
        Assert.assertNotNull(taskSpace.getRequestPath());
        Assert.assertEquals("",taskSpace.getRequestPath());

        Assert.assertNotNull(taskPathed);
        Assert.assertNotNull(taskPathed.getRequestPath());
        Assert.assertEquals("http://gturnquist-quoters.cfapps.io/api/random",taskPathed.getRequestPath());

        //----------------
        Assert.assertNotNull(taskNull5fails);
        Assert.assertNotNull(taskNull5fails.getRequestPath());
        Assert.assertEquals("",taskNull5fails.getRequestPath());

        Assert.assertNotNull(taskSpace5fails);
        Assert.assertNotNull(taskSpace5fails.getRequestPath());
        Assert.assertEquals("",taskSpace5fails.getRequestPath());

        Assert.assertNotNull(taskPathed5fails);
        Assert.assertNotNull(taskPathed5fails.getRequestPath());
        Assert.assertEquals("http://gturnquist-quoters.cfapps.io/api/random",taskPathed5fails.getRequestPath());

        //----------------
        Assert.assertNotNull(taskNull0fails);
        Assert.assertNotNull(taskNull0fails.getRequestPath());
        Assert.assertEquals("",taskNull0fails.getRequestPath());

        Assert.assertNotNull(taskSpace0fails);
        Assert.assertNotNull(taskSpace0fails.getRequestPath());
        Assert.assertEquals("",taskSpace0fails.getRequestPath());

        Assert.assertNotNull(taskPathed0fails);
        Assert.assertNotNull(taskPathed0fails.getRequestPath());
        Assert.assertEquals("http://gturnquist-quoters.cfapps.io/api/random",taskPathed0fails.getRequestPath());
        //----------------
        Assert.assertNotNull(taskNullMinus4);
        Assert.assertNotNull(taskNullMinus4.getRequestPath());
        Assert.assertEquals("",taskNullMinus4.getRequestPath());

        Assert.assertNotNull(taskSpaceMinus4);
        Assert.assertNotNull(taskSpaceMinus4.getRequestPath());
        Assert.assertEquals("",taskSpaceMinus4.getRequestPath());

        Assert.assertNotNull(taskPathedMinus4);
        Assert.assertNotNull(taskPathedMinus4.getRequestPath());
        Assert.assertEquals("http://gturnquist-quoters.cfapps.io/api/random",taskPathedMinus4.getRequestPath());
    }

    @Test
    public void testIncreaseFailsCounter() throws Exception {
        //----------------
        Assert.assertNotNull(taskNull);
        IntStream.range(0, 3).forEach((i)->Assert.assertTrue(taskNull.increaseFailsCounter()));
        Assert.assertNotNull(taskNull);
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskNull.increaseFailsCounter()));

        Assert.assertNotNull(taskSpace);
        IntStream.range(0, 3).forEach((i)->Assert.assertTrue(taskSpace.increaseFailsCounter()));
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskSpace.increaseFailsCounter()));

        Assert.assertNotNull(taskPathed);
        IntStream.range(0, 3).forEach((i)->Assert.assertTrue(taskPathed.increaseFailsCounter()));
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskPathed.increaseFailsCounter()));

        //----------------
        Assert.assertNotNull(taskNull5fails);
        IntStream.range(0, 5).forEach((i)->Assert.assertTrue(taskNull5fails.increaseFailsCounter()));
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskNull5fails.increaseFailsCounter()));

        Assert.assertNotNull(taskSpace5fails);
        IntStream.range(0, 5).forEach((i)->Assert.assertTrue(taskSpace5fails.increaseFailsCounter()));
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskSpace5fails.increaseFailsCounter()));

        Assert.assertNotNull(taskPathed5fails);
        IntStream.range(0, 5).forEach((i)->Assert.assertTrue(taskPathed5fails.increaseFailsCounter()));
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskPathed5fails.increaseFailsCounter()));

        //----------------
        Assert.assertNotNull(taskNull0fails);
        Assert.assertTrue(taskNull0fails.increaseFailsCounter());
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskNull0fails.increaseFailsCounter()));

        Assert.assertNotNull(taskSpace0fails);
        Assert.assertTrue(taskSpace0fails.increaseFailsCounter());
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskSpace0fails.increaseFailsCounter()));

        Assert.assertNotNull(taskPathed0fails);
        Assert.assertTrue(taskPathed0fails.increaseFailsCounter());
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskPathed0fails.increaseFailsCounter()));


        //----------------
        Assert.assertNotNull(taskNullMinus4);
        Assert.assertTrue(taskNullMinus4.increaseFailsCounter());
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskNullMinus4.increaseFailsCounter()));

        Assert.assertNotNull(taskSpaceMinus4);
        Assert.assertTrue(taskSpaceMinus4.increaseFailsCounter());
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskSpaceMinus4.increaseFailsCounter()));

        Assert.assertNotNull(taskPathedMinus4);
        Assert.assertTrue(taskPathedMinus4.increaseFailsCounter());
        IntStream.range(0, 2).forEach((i)->Assert.assertFalse(taskPathedMinus4.increaseFailsCounter()));


    }
}