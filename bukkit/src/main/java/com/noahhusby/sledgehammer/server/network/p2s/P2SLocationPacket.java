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
import com.noahhusby.sledgehammer.server.Sledgehammer;
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
        double x = data.get("x").getAsDouble();
        double z = data.get("z").getAsDouble();

        if (data.has("y")) {
            int y = data.get("y").getAsInt();
            teleport(player, x, y, z);
        } else {
            int xI = (int) Math.floor(x);
            int zI = (int) Math.floor(z);
            if (SledgehammerUtil.hasTerraPlusPlus()) {
                SledgehammerUtil.getTerraConnector().getHeight(x, z).thenAccept(y -> {
                    int height = y.intValue();
                    while (player.getWorld().getBlockAt(xI, height, zI).getType() != Material.AIR &&
                           player.getWorld().getBlockAt(xI, height - 1, zI).getType() != Material.AIR) {
                        height++;
                    }
                    height += 512;
                    while (player.getWorld().getBlockAt(xI, height, zI).getType() == Material.AIR) {
                        height--;
                    }
                    height++;
                    teleport(player, x, height, z);
                });
            } else {
                teleport(player, x, player.getWorld().getHighestBlockYAt(xI, zI) + 1, z);
            }
        }
    }

    private void teleport(Player player, double x, double y, double z) {
        Sledgehammer.getInstance().getLogger().info(String.format("%s > Teleported to %s, %s, %s", player.getName(), x, y, z));
        // TODO: Figure out why direct teleportation is slower
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format("minecraft:tp %s %s %s %s", player.getName(), x, y, z));
        //player.teleport(new Location(player.getWorld(), x, y, z));
    }
}
