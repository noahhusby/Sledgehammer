package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.handlers.WarpHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SetWarpTask extends Task implements IResponse {
    public SetWarpTask(TransferPacket transfer) {
        super(transfer);
    }

    @Override
    public String getResponseCommand() {
        return Constants.setwarpTask;
    }

    @Override
    public void respond(JSONObject d) {
        JSONObject data = (JSONObject) d.get("data");
        JSONObject point = (JSONObject) data.get("point");
        WarpHandler.getInstance().incomingLocationResponse(getSender(), new Point((String) point.get("x"),(String) point.get("y"),
                (String) point.get("z"), (String) point.get("yaw"), (String) point.get("pitch")));
    }

    @Override
    public boolean validateResponse(TransferPacket transfer, JSONObject data) {
        return transfer.sender.equals(getSender());
    }

    @Override
    public String getCommandName() {
        return Constants.setwarpTask;
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
