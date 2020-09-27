/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PWebMapPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.commands.WarpCommand;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import net.md_5.bungee.api.ProxyServer;

public class S2PWebMapPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.webmapID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        new WarpCommand().execute(ProxyServer.getInstance().getPlayer(info.getSender()),
                new String[]{"map"});
    }
}
