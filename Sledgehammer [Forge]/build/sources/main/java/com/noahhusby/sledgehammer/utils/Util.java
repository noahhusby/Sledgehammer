package com.noahhusby.sledgehammer.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

public class Util {
    public static EntityPlayerMP getPlayerFromName(String name) {
        List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
        if(players.isEmpty()) {
            return null;
        }

        for(EntityPlayerMP p : players) {
            if(p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }
}
