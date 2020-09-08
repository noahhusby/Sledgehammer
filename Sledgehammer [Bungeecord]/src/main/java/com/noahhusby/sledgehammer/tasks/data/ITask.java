package com.noahhusby.sledgehammer.tasks.data;

public interface ITask {
    String getCommandName();

    TaskPacket build();

    IResponse getResponse();

}
