/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
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

package com.noahhusby.sledgehammer.server.network.p2s;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.network.P2SPacket;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
        int x = data.get("x").getAsInt();
        int z = data.get("z").getAsInt();

        if (data.has("y")) {
            int y = data.get("y").getAsInt();
            teleport(player.getName(), x, y, z);
        } else {
            if (SledgehammerUtil.hasTerraPlusPlus()) {
                SledgehammerUtil.getTerraConnector().getHeight(x, z).thenAccept(y -> {
                    int height = y.intValue();
                    while (player.getWorld().getBlockAt(x, height, z).getType() != Material.AIR &&
                           player.getWorld().getBlockAt(x, height - 1, z).getType() != Material.AIR) {
                        height++;
                    }
                    teleport(player.getName(), x, height, z);
                });
            } else {
                teleport(player.getName(), x, player.getWorld().getHighestBlockYAt(x, z) + 1, z);
            }
        }
    }

    private void teleport(String player, int x, int y, int z) {
        // TODO: Figure out why I replaced direct player teleportation with tp command
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format("minecraft:tp %s %s %s %s", player, x, y, z));
    }
}
