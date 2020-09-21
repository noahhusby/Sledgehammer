package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.json.simple.JSONObject;

public class InitializationTask extends Task implements IResponse {
    public InitializationTask(TransferPacket transfer) {
        super(transfer);
    }

    @Override
    public String getResponseCommand() {
        return Constants.init;
    }

    @Override
    public void respond(JSONObject d) {
        Sledgehammer.logger.info(Constants.logInitPacket+getServer().getName());
        JSONObject data = (JSONObject) d.get("data");
        ServerConfig.getInstance().initializeServer(getServer(), data);
    }

    @Override
    public boolean validateResponse(TransferPacket transfer, JSONObject data) {
        return transfer.sender.equals(getSender()) && transfer.server.getName().toLowerCase().equals(getServer().getName().toLowerCase());
    }

    @Override
    public String getCommandName() {
        return Constants.init;
    }

    @Override
    public TaskPacket build() {
        return buildPacket(new JSONObject());
    }

    @Override
    public IResponse getResponse() {
        return this;
    }
}
