package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.utils.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TpllTask extends Task {

    private final String lat;
    private final String lon;
    private final CommandType ct;

    public TpllTask(String sender, long executionTime, int time, CommandType ct, String lat, String lon) {
        super(sender, executionTime, time);
        this.lat = lat;
        this.lon = lon;
        this.ct = ct;
    }

    @Override
    public void execute() {
        EntityPlayer player = Util.getPlayerFromName(sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        if(ct.equals(CommandType.CS)) {
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, "/cs tpll "+lat+" "+lon);
            return;
        }

        FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, "/tpll "+lat+" "+lon);
    }

    public enum CommandType {
        CS, TPLL
    }
}
