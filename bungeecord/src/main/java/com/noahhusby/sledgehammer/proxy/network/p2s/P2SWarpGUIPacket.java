/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SWarpGUIPacket.java
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

package com.noahhusby.sledgehammer.proxy.network.p2s;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.network.P2SPacket;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class P2SWarpGUIPacket extends P2SPacket {

    private final String sender;
    private final String server;
    private final boolean editAccess;

    @Override
    public String getPacketID() {
        return Constants.warpGUIID;
    }

    @Override
    public void getMessage(JsonObject data) {
        JsonObject payload = SledgehammerUtil.GSON.toJsonTree(WarpHandler.getInstance().generateGUIPayload(SledgehammerPlayer.getPlayer(sender), editAccess)).getAsJsonObject();
        System.out.println(SledgehammerUtil.GSON.toJson(payload));
        data.add("payload", payload);
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
