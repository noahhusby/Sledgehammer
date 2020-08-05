package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.utils.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TpllTask extends Task {

    private final String lat;
    private final String lon;

    public TpllTask(String sender, long executionTime, int time, String lat, String lon) {
        super(sender, executionTime, time);
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public void execute() {
        EntityPlayer player = Util.getPlayerFromName(sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        if(ConfigHandler.tpllMode.toLowerCase().equals("cs")) {
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, "/cs tpll "+lat+" "+lon);
            return;
        }

        FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, "/tpll "+lat+" "+lon);
    }

}
