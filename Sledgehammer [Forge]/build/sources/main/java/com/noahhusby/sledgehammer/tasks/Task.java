package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Sledgehammer;

public abstract class Task implements ITask {
    public final String sender;
    public final double maxCheckTime;

    public Task(String sender, long executionTime, int time) {
        this.sender = sender;
        if(time > 0) {
            this.maxCheckTime = executionTime + time;
            return;
        }
        maxCheckTime = -1;
    }

    protected void throwNoSender() {
        Sledgehammer.logger.error("The task manager attempted to execute a task without an available sender.");
    }

    protected void throwNoArgs() {
        Sledgehammer.logger.error("The task manager attempted to execute a task without any arguments.");
    }
}
