/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - IS2PPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network;

import org.json.simple.JSONObject;

public interface IS2PPacket {
    String getPacketID();
    JSONObject getMessage(JSONObject data);
    PacketInfo getPacketInfo();
}
