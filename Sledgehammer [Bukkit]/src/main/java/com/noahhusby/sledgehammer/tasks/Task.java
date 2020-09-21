package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.json.simple.JSONObject;

public abstract class Task implements ITask {

    private final TransferPacket transfer;
    private final JSONObject data;
    private final double maxTime;

    public Task(TransferPacket t, JSONObject data) {
        if(t == null) t = new TransferPacket("0.0", "", "");
        if(data == null) data = new JSONObject();
        this.transfer = t;
        this.data = data;
        if(ConfigHandler.executionTimeout > 0) {
            maxTime = getTransferPacket().time + ConfigHandler.executionTimeout;
        } else {
            maxTime = -1;
        }

    }

    public JSONObject getData() {
        return (JSONObject) data.get("data");
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

    public JSONObject generateTaskHeader(JSONObject data) {
        JSONObject o = new JSONObject();
        o.put("uuid", ConfigHandler.authenticationCode);
        o.put("command", getCommandName());
        o.put("sender", getTransferPacket().sender);
        o.put("server", getTransferPacket().server);
        o.put("time", System.currentTimeMillis());
        o.put("data", data);
        return o;
    }
}
