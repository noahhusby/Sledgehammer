/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SLocationPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.json.simple.JSONObject;

public class P2SLocationPacket extends P2SPacket {

    private final String sender;
    private final String server;
    private final String lat;
    private final String lon;

    public P2SLocationPacket(String sender, String server, String lat, String lon) {
        this.server = server;
        this.sender = sender;
        this.lat = lat;
        this.lon = lon;
    }
    @Override
    public String getPacketID() {
        return Constants.locationID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data.put("lat", lat);
        data.put("lon", lon);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
