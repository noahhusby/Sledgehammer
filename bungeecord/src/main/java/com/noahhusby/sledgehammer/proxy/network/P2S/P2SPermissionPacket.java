/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SPermissionPacket.java
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
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.proxy.network.P2S;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.network.P2SPacket;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class P2SPermissionPacket extends P2SPacket {
    private final String server;
    private final SledgehammerPlayer player;
    private final String permission;
    private final String salt;

    @Override
    public String getPacketID() {
        return Constants.permissionCheckID;
    }

    @Override
    public void getMessage(JsonObject data) {
        data.addProperty("salt", salt);
        data.addProperty("player", player.getName());
        data.addProperty("permission", permission);
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player, server);
    }
}
