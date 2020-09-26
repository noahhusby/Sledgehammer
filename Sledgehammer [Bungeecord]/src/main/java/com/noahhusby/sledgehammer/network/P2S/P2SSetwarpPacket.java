package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.json.simple.JSONObject;

public class P2SSetwarpPacket extends P2SPacket {

    private final String server;
    private final String sender;

    public P2SSetwarpPacket(String sender, String server) {
        this.server = server;
        this.sender = sender;
    }

    @Override
    public String getPacketID() {
        return Constants.setwarpID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
