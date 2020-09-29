/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - S2PSetwarpPacket.java
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

public class S2PSetwarpPacket extends S2PPacket {

    private final PacketInfo info;
    private Point point;

    public S2PSetwarpPacket(PacketInfo info) {
        this.info = info;
    }

    @Override
    public String getPacketID() {
        return Constants.setwarpID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        Player p = Bukkit.getPlayer(info.getSender());
        if(p == null) return null;
        if(!p.isOnline()) return null;

        point = new Point(String.valueOf(p.getLocation().getX()), String.valueOf(p.getLocation().getY()), String.valueOf(p.getLocation().getZ()),
                String.valueOf(p.getLocation().getPitch()), String.valueOf(p.getLocation().getYaw()));

        data.put("point", point.getJSON());

        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.renew(info);
    }
}
