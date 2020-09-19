package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class TestLocationTask extends Task implements IResponse {

    private Point point;

    public TestLocationTask(TransferPacket t, JSONObject data) {
        super(t, data);
    }

    @Override
    public String getResponseCommand() {
        return Constants.testLocationTask;
    }

    @Override
    public JSONObject response() {
        JSONObject data = new JSONObject();
        data.put("point", point.getJSON());
        return generateTaskHeader(data);
    }

    @Override
    public String getCommandName() {
        return Constants.testLocationTask;
    }

    @Override
    public void execute() {
        Player p = Bukkit.getPlayer(getTransferPacket().sender);
        if(p == null) {
            throwNoSender();
            return;
        }

        if(!p.isOnline()) {
            throwNoSender();
            return;
        }

        point = new Point(String.valueOf(p.getLocation().getX()), String.valueOf(p.getLocation().getY()), String.valueOf(p.getLocation().getZ()),
                "", "");

        TaskHandler.getInstance().sendResponse(this);
    }

    @Override
    public IResponse getResponse() {
        return this;
    }

    @Override
    public void build(TransferPacket t, JSONObject data) {
        TaskHandler.getInstance().queueTask(new TestLocationTask(t, data));
    }
}
