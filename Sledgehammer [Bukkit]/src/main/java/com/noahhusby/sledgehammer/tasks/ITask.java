package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;

public interface ITask {
    String getCommandName();

    void execute();

    IResponse getResponse();

    void build(TransferPacket t, String[] data);
}
