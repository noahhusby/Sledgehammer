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

package com.noahhusby.sledgehammer.proxy.players;

import com.google.common.collect.ImmutableMap;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.P2S.P2STeleportPacket;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.concurrent.TimeUnit;

/**
 * @author Noah Husby
 * Checks flagged players and teleports them if within region
 */
public class FlaggedBorderCheckerThread implements Runnable {
    @Override
    public void run() {
        ImmutableMap.copyOf(PlayerHandler.getInstance().getPlayers()).forEach((u, p) -> {
            if (!p.isFlagged()) {
                return;
            }
            if (p.checkAttribute("BORDER_MODE", false)) {
                p.setFlagged(false);
                p.setTrackingPoint(null);
                return;
            }

            Point location = p.getLocation();

            double[] proj = SledgehammerUtil.toGeo(location.getX(), location.getZ());
            ServerInfo info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);
            if (info == null) {
                return;
            }
            SledgehammerServer server = ServerHandler.getInstance().getServer(info.getName());
            if (server == null) {
                return;
            }
            if (server.isStealthMode()) {
                return;
            }

            if (!info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                p.connect(info);
                NetworkHandler.getInstance().send(new P2STeleportPacket(p.getName(), info.getName(), location));
                Title title = ProxyServer.getInstance().createTitle();
                title.subTitle(ChatUtil.combine(ChatColor.RED, "You've crossed the border!"));
                title.title(ChatUtil.combine(ChatColor.GRAY, "Teleporting to ", ChatColor.BLUE, info.getName()));
                title.fadeIn(20);
                title.stay(100);
                title.fadeOut(20);

                p.setFlagged(false);

                ProxyServer.getInstance().getScheduler().schedule(Sledgehammer.getInstance(), () -> {
                    p.sendTitle(title);
                    if (!p.checkAttribute("PASSED_BORDER", true)) {
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
