package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class CommandTask extends Task {

    public CommandTask(TransferPacket t, JSONObject data) {
        super(t, data);
    }

    @Override
    public void execute() {
        Player player = SledgehammerUtil.getPlayerFromName(getTransferPacket().sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        Bukkit.getServer().dispatchCommand(player, (String) getData().get("args"));
    }

    @Override
    public String getCommandName() {
        return Constants.commandTask;
    }

    @Override
    public IResponse getResponse() {
        return null;
    }

    @Override
    public void build(TransferPacket t, JSONObject data) {
        TaskHandler.getInstance().queueTask(new CommandTask(t, data));
    }
}
