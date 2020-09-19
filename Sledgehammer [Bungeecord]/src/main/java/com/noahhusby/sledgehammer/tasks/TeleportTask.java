package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.json.simple.JSONObject;

public class TeleportTask extends Task {

    private final Point point;

    public TeleportTask(TransferPacket transfer, Point point) {
        super(transfer);
        this.point = point;
    }

    @Override
    public String getCommandName() {
        return Constants.teleportTask;
    }

    @Override
    public TaskPacket build() {
        JSONObject o = new JSONObject();
        o.put("x", point.x);
        o.put("y", point.y);
        o.put("z", point.z);
        o.put("yaw", point.yaw);
        o.put("pitch", point.pitch);
        return buildPacket(o);
    }

    @Override
    public IResponse getResponse() {
        return null;
    }
}
