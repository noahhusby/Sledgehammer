/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SCommandPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.json.simple.JSONObject;

public class P2SCommandPacket extends P2SPacket {

    private final String server;
    private final String sender;
    private final String[] args;

    public P2SCommandPacket(String sender, String server, String... args) {
        this.server = server;
        this.sender = sender;
        this.args = args;
    }

    @Override
    public String getPacketID() {
        return Constants.commandID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        String a = args[0];

        if(args.length > 1) {
            for(int x = 1; x < args.length; x++) {
                a += " "+args[x];
            }
        }

        data.put("args", a);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
