/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - P2STestLocationPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2P.S2PTestLocationPacket;

public class P2STestLocationPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.testLocationID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        int zoom = (int) ((long) data.get("zoom"));
        getManager().sendPacket(new S2PTestLocationPacket(info, zoom));
    }
}
