/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - FlaggedBorderCheckerThread.java
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

package com.noahhusby.sledgehammer.players;

import com.google.common.collect.ImmutableMap;
import com.noahhusby.sledgehammer.ChatUtil;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.network.P2S.P2STeleportPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Checks flagged players and teleports them if within region
 */
public class FlaggedBorderCheckerThread implements Runnable {
    @Override
    public void run() {
        ImmutableMap.copyOf(PlayerManager.getInstance().getPlayers()).forEach((u, p) -> {
            if(!p.isFlagged()) return;
            if(p.checkAttribute("BORDER_MODE", false)) {
                p.setFlagged(false);
                p.setTrackingPoint(null);
                return;
            }

            Point location = p.getLocation();

            double[] proj = SledgehammerUtil.toGeo(Double.parseDouble(location.x), Double.parseDouble(location.z));
            ServerInfo info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);
            if(info == null) return;
            SledgehammerServer server = ServerConfig.getInstance().getServer(info.getName());
            if(server == null) return;
            if(server.isStealthMode()) return;

            if(!info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                p.connect(info);
                SledgehammerNetworkManager.getInstance().send(new P2STeleportPacket(p.getName(), info.getName(), location));
                Title title = ProxyServer.getInstance().createTitle();
                title.subTitle(ChatUtil.combine(ChatColor.RED, "You've crossed the border!"));
                title.title(ChatUtil.combine(ChatColor.GRAY, "Teleporting to ", ChatColor.BLUE, info.getName()));
                title.fadeIn(20);
                title.stay(100);
                title.fadeOut(20);

                p.setFlagged(false);

                Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                    p.sendTitle(title);
                    if(!p.checkAttribute("PASSED_BORDER", true)) {
                        p.sendMessage(ChatUtil.combine(ChatColor.RED + "" + ChatColor.BOLD, "Reminder: ",
                                ChatColor.GRAY, "You can use ", ChatColor.YELLOW, "/border", ChatColor.GRAY,
                                " to toggle border teleportation!"));
                        p.getAttributes().put("PASSED_BORDER", true);
                    }
                }, 2, TimeUnit.SECONDS);
            }

        });
    }
}
