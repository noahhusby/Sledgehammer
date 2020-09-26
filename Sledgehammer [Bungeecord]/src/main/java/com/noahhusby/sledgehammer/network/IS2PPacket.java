package com.noahhusby.sledgehammer.network;

import com.noahhusby.sledgehammer.SmartObject;

public interface IS2PPacket {
    String getPacketID();
    void onMessage(PacketInfo info, SmartObject data);
}
