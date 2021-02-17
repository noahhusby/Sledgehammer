/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - P2SCommandPacket.java
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

package com.noahhusby.sledgehammer.server.network.P2S;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.network.P2SPacket;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class P2SCommandPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.commandID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        Player player = SledgehammerUtil.getPlayerFromName(info.getSender());
        if(player == null) {
            throwNoSender();
            return;
        }

        Bukkit.getServer().dispatchCommand(player, data.get("args").getAsString());
    }
}
