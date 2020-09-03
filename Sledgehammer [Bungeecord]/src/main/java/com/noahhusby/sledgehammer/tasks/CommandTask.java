package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;

public class CommandTask extends Task {

    private final String[] args;

    public CommandTask(TransferPacket transfer, String... args) {
        super(transfer);
        this.args = args;
    }

    @Override
    public String getCommandName() {
        return Constants.commandTask;
    }

    @Override
    public TaskPacket build() {
        return buildPacket(args);
    }

    @Override
    public IResponse getResponse() {
        return null;
    }
}
