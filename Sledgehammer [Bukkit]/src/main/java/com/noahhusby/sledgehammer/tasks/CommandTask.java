package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandTask extends Task {

    public CommandTask(TransferPacket t, String[] data) {
        super(t, data);
    }

    @Override
    public void execute() {
        Player player = Util.getPlayerFromName(getTransferPacket().sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        if(getData().length == 0) {
            throwNoArgs();
            return;
        }

        if(getData().length < 2) {
            Bukkit.getServer().dispatchCommand(player, getData()[0]);
        } else {
            String command = getData()[0];
            for(int x = 1; x < getData().length; x++) {
                command += " "+getData()[x];
            }
            Bukkit.getServer().dispatchCommand(player, command);
        }
    }

    @Override
    public String getCommandName() {
        return "command";
    }

    @Override
    public IResponse getResponse() {
        return null;
    }

    @Override
    public void build(TransferPacket t, String[] data) {
        TaskHandler.getInstance().queueTask(new CommandTask(t, data));
    }
}
