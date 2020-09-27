/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SWarpGUIPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.handlers.WarpHandler;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.json.simple.JSONObject;

public class P2SWarpGUIPacket extends P2SPacket {

    private final String server;
    private final String sender;

    public P2SWarpGUIPacket(String sender, String server) {
        this.server = server;
        this.sender = sender;
    }

    @Override
    public String getPacketID() {
        return Constants.warpGUIID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        return WarpHandler.getInstance().generateGUIPayload();
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
