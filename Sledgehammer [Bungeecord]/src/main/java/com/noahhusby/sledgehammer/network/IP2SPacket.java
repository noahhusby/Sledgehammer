/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - IP2SPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network;

import org.json.simple.JSONObject;

public interface IP2SPacket {
    String getPacketID();
    JSONObject getMessage(JSONObject data);
    PacketInfo getPacketInfo();
}
