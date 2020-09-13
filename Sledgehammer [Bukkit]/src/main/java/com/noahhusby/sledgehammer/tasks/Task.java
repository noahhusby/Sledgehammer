package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;

public abstract class Task implements ITask {

    private final TransferPacket transfer;
    private final String[] data;
    private final double maxTime;

    public Task(TransferPacket t, String[] data) {
        if(t == null) t = new TransferPacket("0.0", "");
        if(data == null) data = new String[]{};
        this.transfer = t;
        this.data = data;
        if(ConfigHandler.executionTimeout > 0) {
            maxTime = getTransferPacket().time + ConfigHandler.executionTimeout;
        } else {
            maxTime = -1;
        }

    }

    public String[] getData() {
        return data;
    }

    public TransferPacket getTransferPacket() {
        return transfer;
    }

    public double getMaxTime() {
        return maxTime;
    }

    protected void throwNoSender() {
        Sledgehammer.logger.warning("The task manager attempted to execute a task without an available sender.");
    }

    protected void throwNoArgs() {
        Sledgehammer.logger.warning("The task manager attempted to execute a task without any arguments.");
    }
}
