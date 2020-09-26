package com.noahhusby.sledgehammer.network;

import org.json.simple.JSONObject;

public interface IS2PPacket {
    String getPacketID();
    JSONObject getMessage(JSONObject data);
    PacketInfo getPacketInfo();
}
