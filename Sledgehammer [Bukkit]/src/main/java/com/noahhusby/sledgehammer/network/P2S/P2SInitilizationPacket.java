package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2P.S2PInitializationPacket;

public class P2SInitilizationPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.initID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        ConfigHandler.bungeecordName = info.getServer();
        getManager().sendPacket(new S2PInitializationPacket(SledgehammerUtil.getPlayerFromName(info.getSender())));
    }
}
