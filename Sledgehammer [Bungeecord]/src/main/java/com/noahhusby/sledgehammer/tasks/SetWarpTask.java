package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.handlers.WarpHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;

public class SetWarpTask extends Task implements IResponse {
    public SetWarpTask(TransferPacket transfer) {
        super(transfer);
    }

    @Override
    public String getResponseCommand() {
        return Constants.setwarpTask;
    }

    @Override
    public void respond(String[] data) {
        WarpHandler.getInstance().incomingLocationResponse(getSender(), new Point(data[0], data[1], data[2]));
    }

    @Override
    public boolean validateResponse(TransferPacket transfer, String[] data) {
        if(data.length < 3) return false;
        return transfer.sender.equals(getSender());
    }

    @Override
    public String getCommandName() {
        return Constants.setwarpTask;
    }

    @Override
    public TaskPacket build() {
        return buildPacket(new String[0]);
    }

    @Override
    public IResponse getResponse() {
        return this;
    }
}
