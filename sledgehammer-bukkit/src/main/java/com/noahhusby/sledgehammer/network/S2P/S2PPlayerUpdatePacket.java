/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - S2PPlayerUpdatePacket.java
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

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@RequiredArgsConstructor
public class S2PPlayerUpdatePacket extends S2PPacket {

    private final Player player;

    @Override
    public String getPacketID() {
        return Constants.playerUpdateID;
    }

    @Override
    public void getMessage(JsonObject data) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.DOWN);
        Point point = new Point(df.format(player.getLocation().getX()),
                df.format(player.getLocation().getY()), df.format(player.getLocation().getZ()),
                df.format(player.getLocation().getPitch()), df.format(player.getLocation().getYaw()));

        data.add("point", SledgehammerUtil.GSON.toJsonTree(point));
        data.addProperty("gameMode", player.getGameMode().name());
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player);
    }
}
