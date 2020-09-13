package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportTask extends Task {

    public TeleportTask(TransferPacket t, String[] data) {
        super(t, data);
    }

    @Override
    public void execute() {
        String[] data = getData();
        Player player = Util.getPlayerFromName(getTransferPacket().sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        if(!player.isOnline()) {
            throwNoSender();
            return;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("tp %s %s %s %s", getTransferPacket().sender, data[0], data[1], data[2]));
    }

    @Override
    public String getCommandName() {
        return "teleport";
    }

    @Override
    public IResponse getResponse() {
        return null;
    }

    @Override
    public void build(TransferPacket t, String[] data) {
        TaskHandler.getInstance().queueTask(new TeleportTask(t, data));
    }
}
