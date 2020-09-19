package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.tasks.data.ITask;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import net.md_5.bungee.api.config.ServerInfo;
import org.json.simple.JSONObject;

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

    protected TaskPacket buildPacket(JSONObject data) {
        JSONObject o = new JSONObject();
        o.put("command", getCommandName());
        o.put("uuid", ConfigHandler.authenticationCode);
        o.put("time", System.currentTimeMillis());
        o.put("sender", sender);
        o.put("data", data);
        return new TaskPacket(server, sender, o);
    }
}
