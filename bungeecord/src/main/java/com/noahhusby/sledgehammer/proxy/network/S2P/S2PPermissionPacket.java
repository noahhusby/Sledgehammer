/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PPermissionPacket.java
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

package com.noahhusby.sledgehammer.proxy.network.S2P;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.network.S2PPacket;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;

import java.util.UUID;

public class S2PPermissionPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.permissionCheckID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        UUID uuid = UUID.fromString(data.get("player").getAsString());
        SledgehammerPlayer player = PlayerHandler.getInstance().getPlayer(uuid);
        String salt = data.get("salt").getAsString();
        boolean localPermission = data.get("permission").getAsBoolean();
        if (player != null) {
            player.validatePermission(salt, localPermission);
        }
    }
}
