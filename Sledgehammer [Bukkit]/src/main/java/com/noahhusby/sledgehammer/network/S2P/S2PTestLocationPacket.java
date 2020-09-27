/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - S2PTestLocationPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class S2PTestLocationPacket extends S2PPacket {

    private final PacketInfo info;
    private final int zoom;
    private Point point;

    public S2PTestLocationPacket(PacketInfo info, int zoom) {
        this.info = info;
        this.zoom = zoom;
    }

    @Override
    public String getPacketID() {
        return Constants.testLocationID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        Player p = Bukkit.getPlayer(info.getSender());

        point = new Point(String.valueOf(p.getLocation().getX()), String.valueOf(p.getLocation().getY()), String.valueOf(p.getLocation().getZ()),
                "", "");

        data.put("point", point.getJSON());
        data.put("zoom", zoom);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.renew(info);
    }
}
