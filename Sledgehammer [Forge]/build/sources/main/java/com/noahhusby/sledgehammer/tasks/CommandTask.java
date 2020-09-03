package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.utils.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandTask extends Task {

    public CommandTask(TransferPacket t, String[] data) {
        super(t, data);
    }

    @Override
    public void execute() {
        EntityPlayer player = Util.getPlayerFromName(getTransferPacket().sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        if(getData().length == 0) {
            throwNoArgs();
            return;
        }

        if(getData().length < 2) {
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, getData()[0]);
        } else {
            String command = getData()[0];
            for(int x = 1; x < getData().length; x++) {
                command += " "+getData()[x];
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, command);
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
