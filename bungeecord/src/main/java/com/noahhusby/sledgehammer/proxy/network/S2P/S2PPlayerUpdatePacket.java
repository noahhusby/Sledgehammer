/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PPlayerUpdatePacket.java
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
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.network.S2PPacket;
import com.noahhusby.sledgehammer.proxy.players.GameMode;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;

public class S2PPlayerUpdatePacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.playerUpdateID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(info.getSender());
        if (player == null) {
            return;
        }
        Point point = SledgehammerUtil.GSON.fromJson(data.get("point"), Point.class);
        player.setLocation(point);
        player.setGameMode(GameMode.valueOf(data.get("gameMode").getAsString()));
    }
}
