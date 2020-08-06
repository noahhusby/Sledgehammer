package com.noahhusby.sledgehammer.handlers;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.communication.RequestMessage;
import com.noahhusby.sledgehammer.communication.SimpleChannelManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerLocationHandler {
    private static PlayerLocationHandler mInstance = null;

    public static PlayerLocationHandler getInstance() {
        if(mInstance == null) mInstance = new PlayerLocationHandler();
        return mInstance;
    }


    public void onPlayerJoin(EntityPlayerMP player) {
        //sendLocation(player);
    }

    private void sendLocation(EntityPlayerMP player) {
        SimpleChannelManager.network.sendTo(new RequestMessage(String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                "response", ConfigHandler.authenticationCode, System.currentTimeMillis(), player.getName(), "POS",
                player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ())), player);
    }
}
