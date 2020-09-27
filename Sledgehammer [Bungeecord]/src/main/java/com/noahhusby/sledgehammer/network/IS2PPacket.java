/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - IS2PPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network;

import com.noahhusby.sledgehammer.SmartObject;

public interface IS2PPacket {
    String getPacketID();
    void onMessage(PacketInfo info, SmartObject data);
}
