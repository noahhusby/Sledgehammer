/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - S2PWarpPacket.java
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
import com.noahhusby.sledgehammer.common.warps.WarpPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import com.noahhusby.sledgehammer.server.network.S2PPacket;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class S2PWarpPacket extends S2PPacket {
    private final Player player;
    private final WarpPayload payload;
    private final int warpId;

    @Override
    public String getPacketID() {
        return Constants.warpID;
    }

    @Override
    public void getMessage(JsonObject data) {
        data.addProperty("salt", payload.getSalt());
        data.addProperty("warpId", warpId);
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player);
    }
}
