package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.utils.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandTask extends Task {

    String[] args;

    public CommandTask(String sender, long executionTime, int time, String[] args) {
        super(sender, executionTime, time);
        this.args = args;
    }

    @Override
    public void execute() {
        EntityPlayer player = Util.getPlayerFromName(sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        if(args.length < 5) {
            throwNoArgs();
            return;
        }

        if(args.length < 6) {
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, "/"+args[4]);
        } else {
            String command = "/"+args[4];
            for(int x = 5; x < args.length; x++) {
                command += " "+args[x];
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, command);
        }
    }
}
