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

package com.noahhusby.sledgehammer.server.network.S2P;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import com.noahhusby.sledgehammer.server.network.S2PPacket;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class S2PSetwarpPacket extends S2PPacket {

    private final PacketInfo info;

    @Override
    public String getPacketID() {
        return Constants.setwarpID;
    }

    @Override
    public void getMessage(JsonObject data) {
        Player p = Bukkit.getPlayer(info.getSender());
        if (p == null) {
            return;
        }
        if (!p.isOnline()) {
            return;
        }
        Location loc = p.getLocation();
        Point point =  new Point(loc.getX(), loc.getY(), loc.getZ(), loc.getY(), loc.getPitch());
        data.add("point", SledgehammerUtil.GSON.toJsonTree(point));
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.renew(info);
    }
}
