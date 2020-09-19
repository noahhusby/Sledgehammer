package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.json.simple.JSONObject;

public interface ITask {
    String getCommandName();

    void execute();

    IResponse getResponse();

    void build(TransferPacket t, JSONObject data);
}
