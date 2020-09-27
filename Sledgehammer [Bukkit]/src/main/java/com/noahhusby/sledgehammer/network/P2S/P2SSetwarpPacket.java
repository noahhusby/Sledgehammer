/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - P2SSetwarpPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2P.S2PSetwarpPacket;

public class P2SSetwarpPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.setwarpID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        getManager().sendPacket(new S2PSetwarpPacket(info));
    }
}
