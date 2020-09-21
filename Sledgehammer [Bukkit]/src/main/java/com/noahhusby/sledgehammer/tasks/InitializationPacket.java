package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class InitializationPacket extends Task implements IResponse {

    private Point point;

    public InitializationPacket(TransferPacket t, JSONObject data) {
        super(t, data);
    }

    @Override
    public String getResponseCommand() {
        return Constants.init;
    }

    @Override
    public JSONObject response() {
        JSONObject data = new JSONObject();
        data.put("version", Constants.VERSION);
        data.put("tpllmode", ConfigHandler.tpllMode);
        return generateTaskHeader(data);
    }

    @Override
    public String getCommandName() {
        return Constants.init;
    }

    @Override
    public void execute() {
        TaskHandler.getInstance().sendResponse(this);
    }

    @Override
    public IResponse getResponse() {
        return this;
    }

    @Override
    public void build(TransferPacket t, JSONObject data) {
        TaskHandler.getInstance().queueTask(new InitializationPacket(t, data));
    }
}
