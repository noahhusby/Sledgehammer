package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.tasks.data.ITask;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import net.md_5.bungee.api.config.ServerInfo;

public abstract class Task implements ITask {

    private final ServerInfo server;
    private final String sender;

    public Task(TransferPacket transfer) {
        this.server = transfer.server;
        this.sender = transfer.sender;
    }

    protected ServerInfo getServer() {
        return server;
    }

    protected String getSender() {
        return sender;
    }

    protected TaskPacket buildPacket(String[] data) {
        return new TaskPacket(server, sender, data);
    }
}
