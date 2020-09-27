/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - S2PWarpPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class S2PWarpPacket extends S2PPacket {

    private final Player player;
    private final String warp;

    public S2PWarpPacket(Player player, String warp) {
        this.player = player;
        this.warp = warp;
    }

    @Override
    public String getPacketID() {
        return Constants.warpID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data.put("warp", warp);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player);
    }
}
