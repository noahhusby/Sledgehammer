package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;

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
        String[] data = {point.x, point.y, point.z};
        return buildPacket(data);
    }

    @Override
    public IResponse getResponse() {
        return null;
    }
}
