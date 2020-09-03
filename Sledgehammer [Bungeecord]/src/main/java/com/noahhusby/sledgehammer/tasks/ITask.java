package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.tasks.data.TaskPacket;

public interface ITask {
    String getCommandName();

    TaskPacket build();

    IResponse getResponse();

}
