/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SWarpConfigPacket.java
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
import com.noahhusby.lib.data.JsonUtils;
import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.network.P2SPacket;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class P2SWarpConfigPacket extends P2SPacket {

    private final SledgehammerPlayer player;
    private final WarpConfigPayload.ServerConfigAction action;
    private final boolean admin;
    private JsonObject data = new JsonObject();

    @Override
    public String getPacketID() {
        return Constants.warpConfigID;
    }

    @Override
    public void getMessage(JsonObject data) {
        WarpConfigPayload payload = WarpHandler.getInstance().generateConfigPayload(player, admin);
        payload.setAction(action);
        for (String key : JsonUtils.keySet(this.data)) {
            data.add(key, this.data.get(key));
        }
        data.add("payload", SledgehammerUtil.GSON.toJsonTree(payload));
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player, player.getServer().getInfo().getName());
    }
}
