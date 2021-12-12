/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.noahhusby.sledgehammer.server.network.s2p;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.network.P2SPacket;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class P2SLocationPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.locationID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        Player player = Bukkit.getPlayer(info.getSender());
        if (player == null) {
            throwNoSender();
            return;
        }

        String lat = data.get("lat").getAsString();
        String lon = data.get("lon").getAsString();

        int xOffset = 0;
        int zOffset = 0;

        try {
            xOffset = Integer.parseInt(data.get("xOffset").getAsString());
            zOffset = Integer.parseInt(data.get("zOffset").getAsString());
        } catch (Exception ignored) {
        }

        double[] proj = SledgehammerUtil.fromGeo(Double.parseDouble(lon), Double.parseDouble(lat));

        int x = (int) Math.floor(proj[0]) + xOffset;
        int z = (int) Math.floor(proj[1]) + zOffset;

        if (SledgehammerUtil.hasTerraPlusPlus()) {
            SledgehammerUtil.getTerraConnector().getHeight(x, z).thenAccept(y -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format("minecraft:tp %s %s %s %s", player.getName(), x, y, z)));
        } else {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format("minecraft:tp %s %s %s %s", player.getName(), x, player.getWorld().getHighestBlockYAt(x, z) + 1, z));
        }
    }
}
