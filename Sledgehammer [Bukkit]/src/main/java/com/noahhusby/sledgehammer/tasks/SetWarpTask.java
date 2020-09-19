package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.List;

public class SetWarpTask extends Task implements IResponse {

    private Point point;

    public SetWarpTask(TransferPacket t, JSONObject data) {
        super(t, data);
    }

    @Override
    public String getResponseCommand() {
        return Constants.setwarpTask;
    }

    @Override
    public JSONObject response() {
        JSONObject data = new JSONObject();
        data.put("point", point.getJSON());
        return generateTaskHeader(data);
    }

    @Override
    public String getCommandName() {
        return Constants.setwarpTask;
    }

    @Override
    public void execute() {
        Player p = Bukkit.getPlayer(getTransferPacket().sender);
        if(p == null) return;
        if(!p.isOnline()) return;

        point = new Point(String.valueOf(p.getLocation().getX()), String.valueOf(p.getLocation().getY()), String.valueOf(p.getLocation().getZ()),
                String.valueOf(p.getLocation().getPitch()), String.valueOf(p.getLocation().getYaw()));

        TaskHandler.getInstance().sendResponse(this);
    }

    @Override
    public IResponse getResponse() {
        return this;
    }

    @Override
    public void build(TransferPacket t, JSONObject data) {
        TaskHandler.getInstance().queueTask(new SetWarpTask(t, data));
    }
}
