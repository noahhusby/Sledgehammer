/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - S2PTestLocationPacket.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
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
