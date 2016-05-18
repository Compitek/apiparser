package net.compitek.apiparser.engine;

/**
 * Created by Evgene on 05.04.2016.
 */
public interface ITaskQueue {
    Task getTask();

    void putPrimarily(Task task);

    void putRepeated(Task task);

    /**
     * wait until task source insert all tasks in taskQueue;
     */
    void waitTasksOver();
}
