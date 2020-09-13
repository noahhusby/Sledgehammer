package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TestLocationTask extends Task implements IResponse {

    private Point point;

    public TestLocationTask(TransferPacket t, String[] data) {
        super(t, data);
    }

    @Override
    public String getResponseCommand() {
        return Constants.testLocationTask;
    }

    @Override
    public String[] response() {
        return new String[]{point.x, point.y, point.z};
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

        point = new Point(String.valueOf(p.getLocation().getX()), String.valueOf(p.getLocation().getY()), String.valueOf(p.getLocation().getZ()));

        TaskHandler.getInstance().sendResponse(this);
    }

    @Override
    public IResponse getResponse() {
        return this;
    }

    @Override
    public void build(TransferPacket t, String[] data) {
        TaskHandler.getInstance().queueTask(new TestLocationTask(t, data));
    }
}
