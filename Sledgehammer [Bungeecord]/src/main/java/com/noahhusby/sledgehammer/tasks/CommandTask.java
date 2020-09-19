package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.json.simple.JSONObject;

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
        JSONObject data = new JSONObject();
        String a = args[0];
        for(int x = 1; x < a.length(); x++) {
            a += " "+args[x];
        }
        data.put("args", a);
        return buildPacket(data);
    }

    @Override
    public IResponse getResponse() {
        return null;
    }
}
