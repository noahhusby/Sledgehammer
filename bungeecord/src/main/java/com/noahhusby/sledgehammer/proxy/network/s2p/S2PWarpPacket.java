/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PWarpPacket.java
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

package com.noahhusby.sledgehammer.proxy.network.s2p;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.network.S2PPacket;
import com.noahhusby.sledgehammer.proxy.network.p2s.P2STeleportPacket;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;

public class S2PWarpPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.warpID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(info.getSender());
        if (player == null || !player.validateAction(data.get("salt").getAsString())) {
            return;
        }
        int warpId = data.get("warpId").getAsInt();

        Warp warp = WarpHandler.getInstance().getWarp(warpId);

        if (warp == null) {
            player.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "That warp does not exist!"));
            return;
        }

        if (SledgehammerUtil.getServerFromSender(player) != SledgehammerUtil.getServerByName(warp.getServer())) {
            player.connect(SledgehammerUtil.getServerByName(warp.getServer()));
            player.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Sending you to ", ChatColor.RED, warp.getServer()));
        }

        player.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Warping to ", ChatColor.RED, warp.getName()));
        NetworkHandler.getInstance().send(new P2STeleportPacket(player.getName(), warp.getServer(), warp.getPoint()));
    }
}
