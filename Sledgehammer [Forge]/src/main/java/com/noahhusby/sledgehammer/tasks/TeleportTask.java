package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.utils.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TeleportTask extends Task {

    private final String x;
    private final String y;
    private final String z;

    public TeleportTask(String sender, long executionTime, int time, String x, String y, String z) {
        super(sender, executionTime, time);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void execute() {
        EntityPlayer player = Util.getPlayerFromName(sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, String.format("tp %s %s %s", x, y, z));
    }
}
