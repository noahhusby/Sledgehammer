/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - IP2SPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network;

import com.noahhusby.sledgehammer.SmartObject;
import org.json.simple.JSONObject;

public interface IP2SPacket {
    String getPacketID();
    void onMessage(PacketInfo info, SmartObject data);
}
