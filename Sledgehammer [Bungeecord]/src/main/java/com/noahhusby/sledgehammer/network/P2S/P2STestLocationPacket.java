/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2STestLocationPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.json.simple.JSONObject;

public class P2STestLocationPacket extends P2SPacket {
    private final String server;
    private final String sender;
    private int zoom;

    public P2STestLocationPacket(String sender, String server, int zoom) {
        this.server = server;
        this.sender = sender;
        this.zoom = zoom;
        if(zoom == -1) this.zoom = ConfigHandler.zoom;
    }
    @Override
    public String getPacketID() {
        return Constants.testLocationID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data.put("zoom", zoom);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
